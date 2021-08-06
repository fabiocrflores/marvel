package com.marvel.developer.rest.errors.network

import com.marvel.developer.domain.errors.NetworkingError
import io.reactivex.rxjava3.core.*
import org.reactivestreams.Publisher
import java.io.IOException
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class NetworkingErrorTransformer<T> : SingleTransformer<T, T> {

    override fun apply(upstream: Single<T>): SingleSource<T> {
        return upstream.onErrorResumeNext(this::handleIfNetworkingError)
    }

    private fun handleIfNetworkingError(error: Throwable) =
        if (isNetworkingError(error)) asNetworkingError(error)
        else Single.error(error)

    private fun asNetworkingError(error: Throwable) = Single.error<T>(
        mapToDomainError(error)
    )

    private fun mapToDomainError(error: Throwable): NetworkingError {
        if (isConnectionTimeout(error)) return NetworkingError.OperationTimeout
        if (cannotReachHost(error)) return NetworkingError.HostUnreachable
        return NetworkingError.ConnectionSpike
    }

    private fun isNetworkingError(error: Throwable) =
        isConnectionTimeout(error) || cannotReachHost(error) || isRequestCanceled(error)

    private fun isRequestCanceled(error: Throwable) =
        error is IOException && error.message?.contentEquals("Canceled") ?: false

    private fun cannotReachHost(error: Throwable) =
        error is UnknownHostException ||
                error is ConnectException ||
                error is NoRouteToHostException

    private fun isConnectionTimeout(error: Throwable) =
        error is SocketTimeoutException
}