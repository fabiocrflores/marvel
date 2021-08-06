package com.marvel.developer.shareutils.bases.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewbinding.ViewBinding
import com.marvel.developer.shareutils.Disposer
import javax.inject.Inject

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

@Suppress("UNCHECKED_CAST")
abstract class BaseFragmentViewBinding<VB : ViewBinding>(
    private val inflate: Inflate<VB>
) : Fragment() {

    @Inject
    lateinit var disposer: Disposer

    private var _binding: VB? = null
    protected val binding get() = _binding!!
    protected lateinit var mContext: Context
    protected lateinit var mActivity: FragmentActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mContext = context
        mActivity = requireActivity()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return bind(inflater, container)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    open fun bind(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = inflate.invoke(inflater, container, false)

        return binding.root
    }
}