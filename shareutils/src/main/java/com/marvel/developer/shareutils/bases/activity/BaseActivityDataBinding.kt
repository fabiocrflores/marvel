package com.marvel.developer.shareutils.bases.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.marvel.developer.shareutils.Disposer
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
abstract class BaseActivityDataBinding<VDB : ViewDataBinding>(val view: Int) : AppCompatActivity() {

    @Inject
    lateinit var disposer: Disposer

    protected lateinit var binding: VDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(bind())
    }

    open fun bind(): View {
        binding = DataBindingUtil.inflate(layoutInflater, view, null, false)
        binding.lifecycleOwner = this

        return binding.root
    }
}