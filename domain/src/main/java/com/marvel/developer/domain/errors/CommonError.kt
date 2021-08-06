package com.marvel.developer.domain.errors

sealed class CommonError : Throwable() {

    object NoConnection : CommonError()
    object NoResultsFound : CommonError()

    override fun toString() = when (this) {
        NoConnection -> "No connection"
        NoResultsFound -> "No results found"
    }
}