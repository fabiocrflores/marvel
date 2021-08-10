package com.marvel.developer.rest.utils

fun Any.loadFiles(path: String) =
    this.javaClass
        .classLoader!!
        .getResourceAsStream(path)
        .bufferedReader()
        .use { it.readText() }