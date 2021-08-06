package com.marvel.developer.shareutils.widgets.manyfacedview.view

import android.animation.Animator
import android.animation.AnimatorInflater
import android.content.Context
import android.content.res.TypedArray
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import com.marvel.developer.shareutils.R
import com.marvel.developer.shareutils.widgets.manyfacedview.animator.ActionCallback
import com.marvel.developer.shareutils.widgets.manyfacedview.animator.AnimatorComposer
import com.marvel.developer.shareutils.widgets.manyfacedview.view.FacedViewState.Companion.CONTENT
import com.marvel.developer.shareutils.widgets.manyfacedview.view.FacedViewState.Companion.EMPTY
import com.marvel.developer.shareutils.widgets.manyfacedview.view.FacedViewState.Companion.ERROR
import com.marvel.developer.shareutils.widgets.manyfacedview.view.FacedViewState.Companion.LOADING
import com.marvel.developer.shareutils.widgets.manyfacedview.view.FacedViewState.Companion.UNKNOWN

class ManyFacedView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) :
    FrameLayout(context, attrs, defStyle) {

    private lateinit var layoutInflater: LayoutInflater

    private val stateViews: SparseArray<View> = SparseArray()

    @FacedViewState
    private var previousState: Int = UNKNOWN

    @FacedViewState
    private var initialState: Int = UNKNOWN

    @FacedViewState
    private var currentState: Int = UNKNOWN

    private var animateTransition = false
    private var inAnimator: Animator? = null
    private var outAnimator: Animator? = null
    private var animatorComposer: AnimatorComposer? = null

    private var listener: OnStateChangedListener? = null

    init {
        initialize()
        attrs?.let { parseAttributes(it) }
        setupView()
    }

    private fun initialize() {
        layoutInflater = LayoutInflater.from(context)
    }

    private fun parseAttributes(attrs: AttributeSet) {
        val attributes = resources.obtainAttributes(attrs, R.styleable.ManyFacedView)

        val hasContentView = inflateViewForStateIfExists(
            attributes,
            CONTENT,
            R.styleable.ManyFacedView_mfv_content
        )
        check(hasContentView) { "Missing view for content" }

        inflateViewForStateIfExists(attributes, EMPTY, R.styleable.ManyFacedView_mfv_empty)
        inflateViewForStateIfExists(attributes, ERROR, R.styleable.ManyFacedView_mfv_error)
        inflateViewForStateIfExists(
            attributes,
            LOADING,
            R.styleable.ManyFacedView_mfv_loading
        )

        if (attributes.hasValue(R.styleable.ManyFacedView_mfv_state)) {
            initialState = attributes.getInt(R.styleable.ManyFacedView_mfv_state, UNKNOWN)
        }

        animateTransition = attributes.getBoolean(
            R.styleable.ManyFacedView_mfv_animateChanges,
            true
        )

        val inAnimationId = attributes.getResourceId(
            R.styleable.ManyFacedView_mfv_inAnimation,
            R.animator.mfv_fade_in
        )
        inAnimator = AnimatorInflater.loadAnimator(context, inAnimationId)

        val outAnimationId = attributes.getResourceId(
            R.styleable.ManyFacedView_mfv_outAnimation,
            R.animator.mfv_fade_out
        )
        outAnimator = AnimatorInflater.loadAnimator(context, outAnimationId)
        attributes.recycle()
    }

    private fun setupView() {
        val newState = if (initialState == UNKNOWN) CONTENT else initialState
        setState(newState)
    }

    private fun inflateViewForStateIfExists(
        typedArray: TypedArray,
        @FacedViewState state: Int,
        styleable: Int
    ): Boolean {
        if (typedArray.hasValue(styleable)) {
            val layoutId = typedArray.getResourceId(styleable, 0)
            addStateView(state, layoutId)

            return true
        }

        return false
    }

    private fun addStateView(@FacedViewState state: Int, view: View) {
        stateViews.put(state, view)
        if (currentState != state) view.visibility = INVISIBLE
        addView(view)
    }

    private fun addStateView(@FacedViewState state: Int, @LayoutRes layoutId: Int) {
        val inflatedView = layoutInflater.inflate(layoutId, this, false)
        addStateView(state, inflatedView)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : View> getView(@FacedViewState state: Int): T = stateViews.get(state) as T

    fun hasStateViewAttached(): Boolean = childCount > 0

    @FacedViewState
    fun getState(): Int = this.currentState

    fun setState(@FacedViewState state: Int) {
        if (currentState == state) return

        require(stateViews.indexOfKey(state) >= 0) {
            "Attempting to set a state without setting its view ($state)"
        }

        val inView = stateViews.get(state)
        val outView = stateViews.get(currentState)

        previousState = currentState
        currentState = state

        if (animateTransition) animateViewSwap(outView, inView) else immediateSwap(outView, inView)
    }

    private fun animateViewSwap(outView: View?, inView: View) {
        cancelPreviousAnimation()

        animateLollipopAndNewer(outView, inView)
    }

    private fun animateLollipopAndNewer(outView: View?, inView: View) {
        animateInAndOutView(outView, inView)
    }

    private fun animateInAndOutView(outView: View?, inView: View) {
        inView.visibility = INVISIBLE

        animatorComposer = AnimatorComposer
            .from(outAnimator, outView)
            .nextAction(object : ActionCallback {
                override fun execute() {
                    hideOutView(outView)
                    inView.visibility = VISIBLE
                }
            })
            .next(inAnimator, inView)
            .nextAction(object : ActionCallback {
                override fun execute() {
                    notifyStateChanged()
                }
            })
            .start()
    }

    private fun animateInView(inView: View) {
        inView.visibility = VISIBLE

        animatorComposer = AnimatorComposer
            .from(inAnimator, inView)
            .nextAction(object : ActionCallback {
                override fun execute() {
                    notifyStateChanged()
                }
            })
            .start()
    }

    private fun cancelPreviousAnimation() {
        if (animatorComposer != null) animatorComposer?.stop()
    }

    private fun immediateSwap(outView: View?, inView: View) {
        hideOutView(outView)
        inView.visibility = VISIBLE
        notifyStateChanged()
    }

    private fun notifyStateChanged() {
        if (listener != null && previousState != UNKNOWN) listener?.onChanged(currentState)
    }

    private fun hideOutView(outView: View?) {
        outView?.let { it.visibility = INVISIBLE }
    }

    fun enableTransitionAnimation(enable: Boolean) {
        animateTransition = enable
    }

    fun setInAnimator(inAnimator: Animator) {
        this.inAnimator = inAnimator
    }

    fun setOutAnimator(outAnimator: Animator) {
        this.outAnimator = outAnimator
    }

    fun setOnStateChangedListener(listener: OnStateChangedListener?) {
        this.listener = listener
    }

    override fun onSaveInstanceState(): Parcelable {
        val parentState = super.onSaveInstanceState()
        val savedState = SavedState(parentState)
        savedState.currentState = currentState
        savedState.previousState = previousState

        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val savedState = state as SavedState
        super.onRestoreInstanceState(state.superState)

        currentState = previousState
        setState(savedState.currentState)
    }

    class SavedState : BaseSavedState {

        @FacedViewState
        var currentState: Int = UNKNOWN

        @FacedViewState
        var previousState: Int = UNKNOWN

        constructor(superState: Parcelable?) : super(superState)

        private constructor(parcel: Parcel?) : super(parcel) {
            this.currentState = parcel?.readInt() ?: UNKNOWN
            this.previousState = parcel?.readInt() ?: UNKNOWN
        }

        override fun writeToParcel(out: Parcel?, flags: Int) {
            super.writeToParcel(out, flags)
            out?.writeInt(currentState)
            out?.writeInt(previousState)
        }

        override fun describeContents(): Int = 0

        companion object {
            @JvmField
            val CREATOR = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(source: Parcel?) = SavedState(source)
                override fun newArray(size: Int) = arrayOfNulls<SavedState>(size)
            }
        }
    }
}