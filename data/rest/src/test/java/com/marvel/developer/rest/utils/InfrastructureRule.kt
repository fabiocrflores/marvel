package com.marvel.developer.rest.utils

import com.marvel.developer.rest.BuildRetrofit
import com.marvel.developer.rest.api.MarvelAPI
import com.marvel.developer.rest.interceptor.ApiKeyInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.rules.ExternalResource

internal class InfrastructureRule : ExternalResource() {

    private lateinit var server: MockWebServer
    lateinit var marvelAPI: MarvelAPI

    override fun before() {
        super.before()
        server = MockWebServer()
        val url = server.url("/").toString()
        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor())
            .addInterceptor(ApiKeyInterceptor())
            .build()

        marvelAPI = BuildRetrofit(url, client).create(MarvelAPI::class.java)
    }

    override fun after() {
        server.shutdown()
        super.after()
    }

    fun defineScenario(status: Int, response: String = "") {
        server.enqueue(
            MockResponse().apply {
                setResponseCode(status)
                setBody(response)
            }
        )
    }
}