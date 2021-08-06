package com.marvel.developer.rest.errors.network

import com.google.gson.JsonIOException
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.marvel.developer.domain.errors.RemoteServiceIntegrationError
import io.reactivex.rxjava3.core.*
import org.reactivestreams.Publisher
import timber.log.Timber

class SerializationErrorTransformer<T> : SingleTransformer<T, T> {

    override fun apply(upstream: Single<T>): SingleSource<T> {
        return upstream.onErrorResumeNext(this::handleSerializationError)
    }

    private fun handleSerializationError(error: Throwable): Single<T> {
        error.message?.let { Timber.e(error) } ?: error.printStackTrace()

        val mapped = when (error) {
            is IllegalStateException,
            is JsonIOException,
            is JsonSyntaxException,
            is JsonParseException -> RemoteServiceIntegrationError.UnexpectedResponse
            else -> error
        }

        return Single.error(mapped)
    }
}