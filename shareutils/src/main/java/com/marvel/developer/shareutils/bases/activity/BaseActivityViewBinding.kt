package com.marvel.developer.shareutils.bases.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.marvel.developer.shareutils.Disposer
import javax.inject.Inject

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

@Suppress("UNCHECKED_CAST")
abstract class BaseActivityViewBinding<VB : ViewBinding>(
    private val inflate: Inflate<VB>
) : AppCompatActivity() {

    @Inject
    lateinit var disposer: Disposer

    protected lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(bind())
    }

    open fun bind(): View {
        binding = inflate.invoke(layoutInflater, null, false)

        return binding.root
    }
}