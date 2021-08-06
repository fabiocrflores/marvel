package com.marvel.developer.shareutils

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import timber.log.Timber

class Disposer : DefaultLifecycleObserver {

    private val trash by lazy {
        CompositeDisposable()
    }

//    override fun onStop(owner: LifecycleOwner) {
//        Timber.i("Disposing at onStop -> ${trash.size()} items")
//        trash.clear()
//        super.onStop(owner)
//    }

    override fun onDestroy(owner: LifecycleOwner) {
        Timber.i("Disposing at onDestroy -> ${trash.size()} items")
        trash.dispose()
        super.onDestroy(owner)
    }

    fun collect(target: Disposable) {
        trash.add(target)
    }
}