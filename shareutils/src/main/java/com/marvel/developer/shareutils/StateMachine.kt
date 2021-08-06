package com.marvel.developer.shareutils

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.FlowableTransformer
import org.reactivestreams.Publisher

class StateMachine<T> : FlowableTransformer<T, ViewState<T>> {

    @Suppress("USELESS_CAST")
    override fun apply(upstream: Flowable<T>): Publisher<ViewState<T>> {

        val beginning = Flowable.just(ViewState.Launched)
        val end = Flowable.just(ViewState.Done)

        return upstream
            .map { value: T -> ViewState.Success(value) as ViewState<T> }
            .onErrorReturn { error: Throwable -> ViewState.Failed(error) }
            .startWith(beginning)
            .concatWith(end)
            .observeOn(mainThread())
    }
}