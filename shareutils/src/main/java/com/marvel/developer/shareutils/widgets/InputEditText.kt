package com.marvel.developer.shareutils.widgets

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.doOnTextChanged
import com.marvel.developer.shareutils.R

class InputEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : AppCompatEditText(context, attrs, defStyle) {

    companion object {
        private val STATE_ERROR = intArrayOf(R.attr.state_error)
    }

    private var isError = false

    init {
        doOnTextChanged { _, _, _, _ ->
            setError(false)
        }
    }

    override fun setError(error: CharSequence?) {
        isError = error != null
        super.setError(error)
        refreshDrawableState()
    }

    override fun setError(error: CharSequence?, icon: Drawable?) {
        isError = error != null
        super.setError(error, icon)
        refreshDrawableState()
    }

    fun setError(error: Boolean) {
        isError = error
        refreshDrawableState()
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        var drawableState = super.onCreateDrawableState(extraSpace + 1)
        if (isError) {
            drawableState = View.mergeDrawableStates(drawableState, STATE_ERROR)
        }

        return drawableState
    }
}