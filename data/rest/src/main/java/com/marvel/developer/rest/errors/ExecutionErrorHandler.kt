package com.marvel.developer.rest.errors

import com.marvel.developer.rest.errors.network.HttpIntegrationErrorTransformer
import com.marvel.developer.rest.errors.network.NetworkingErrorTransformer
import com.marvel.developer.rest.errors.network.SerializationErrorTransformer
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleSource
import io.reactivex.rxjava3.core.SingleTransformer
import timber.log.Timber

class ExecutionErrorHandler<T> : SingleTransformer<T, T> {

    override fun apply(upstream: Single<T>): SingleSource<T> =
        upstream
            .compose(HttpIntegrationErrorTransformer())
            .compose(NetworkingErrorTransformer())
            .compose(SerializationErrorTransformer())
            .doOnError { Timber.e("API integration | Failed with -> $it") }
            .doOnSuccess { Timber.v("API integration -> Success") }
}