package com.marvel.developer.rest

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object BuildRetrofit {

    operator fun invoke(httpClient: OkHttpClient): Retrofit =
        with(Retrofit.Builder()) {
            baseUrl("https://gateway.marvel.com/")
            client(httpClient)
            addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            addConverterFactory(ScalarsConverterFactory.create())
            addConverterFactory(GsonConverterFactory.create())
            build()
        }
}