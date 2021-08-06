package com.marvel.developer.shareutils.bases.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.inflate
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.marvel.developer.shareutils.Disposer
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
abstract class BaseFragmentDataBinding<VDB : ViewDataBinding>(val view: Int) : Fragment() {

    @Inject
    lateinit var disposer: Disposer

    protected lateinit var binding: VDB
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

    open fun bind(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = inflate(inflater, view, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }
}