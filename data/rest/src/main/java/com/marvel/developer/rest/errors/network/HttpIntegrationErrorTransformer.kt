package com.marvel.developer.rest.errors.network

import com.marvel.developer.domain.errors.RemoteServiceIntegrationError
import io.reactivex.rxjava3.core.*
import org.reactivestreams.Publisher
import retrofit2.HttpException

class HttpIntegrationErrorTransformer<T> : SingleTransformer<T, T> {

    companion object {
        private const val FIRST_HTTP_CODE = 400
        private const val LAST_HTTP_CODE = 499
    }

    override fun apply(upstream: Single<T>): SingleSource<T> {
        return upstream.onErrorResumeNext(this::handleIfRestError)
    }

    private fun handleIfRestError(incoming: Throwable): Single<T> =
        if (incoming is HttpException) toInfrastructureError(incoming)
        else Single.error(incoming)

    private fun toInfrastructureError(restError: HttpException): Single<T> {
        val infraError = translateUsingStatusCode(restError.code())
        return Single.error(infraError)
    }

    private fun translateUsingStatusCode(code: Int) = when (code) {
        in FIRST_HTTP_CODE..LAST_HTTP_CODE -> RemoteServiceIntegrationError.ClientOrigin
        else -> RemoteServiceIntegrationError.RemoteSystem
    }
}