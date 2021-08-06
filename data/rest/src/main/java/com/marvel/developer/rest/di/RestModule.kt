package com.marvel.developer.rest.di

import android.app.Application
import com.marve.developer.rest.BuildConfig
import com.marvel.developer.domain.services.CharacterService
import com.marvel.developer.rest.BuildRetrofit
import com.marvel.developer.rest.api.MarvelAPI
import com.marvel.developer.rest.errors.ExecutionErrorHandler
import com.marvel.developer.rest.infrastructures.CharacterInfrastructure
import com.marvel.developer.rest.interceptor.ApiKeyInterceptor
import com.marvel.developer.rest.mappers.character.CharacterDataMapper
import com.marvel.developer.rest.mappers.character.CharacterMapper
import com.marvel.developer.rest.mappers.character.CharacterResultMapper
import com.marvel.developer.rest.responses.character.CharacterResultResponse
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RestModule {

    @Binds
    @Singleton
    abstract fun bindCharacterService(infrastructure: CharacterInfrastructure): CharacterService

    companion object {
        private const val TIMEOUT = 60L
        private const val FOLDER_CACHE_NAME = "http-cache"
        private const val CACHE_SIZE = 10 * 1024 * 1024 // 10 MB

        @Provides
        @Singleton
        fun provideHttpLogging(): HttpLoggingInterceptor =
            HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                else HttpLoggingInterceptor.Level.NONE
            }

        @Provides
        @Singleton
        fun provideCache(application: Application): Cache {
            val cacheSize = CACHE_SIZE.toLong()
            val httpCacheDirectory = File(application.cacheDir, FOLDER_CACHE_NAME)

            return Cache(httpCacheDirectory, cacheSize)
        }

        @Provides
        @Singleton
        fun provideOkHttpClient(
            cache: Cache,
            logger: HttpLoggingInterceptor,
        ): OkHttpClient {
            val builder = OkHttpClient.Builder()
//                .cache(cache)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(ApiKeyInterceptor())
                .addInterceptor(logger)

            return builder.build()
        }

        @Provides
        @Singleton
        fun provideRetrofit(client: OkHttpClient): Retrofit = BuildRetrofit(client)

        @Provides
        @Singleton
        fun provideMarvelApi(retrofit: Retrofit): MarvelAPI =
            retrofit.create(MarvelAPI::class.java)

        @Provides
        @Singleton
        fun provideCharacterResponseErrorHandler(): ExecutionErrorHandler<CharacterResultResponse> =
            ExecutionErrorHandler()

        @Provides
        @Singleton
        fun provideCharacterResultMapper(): CharacterResultMapper =
            CharacterResultMapper(CharacterDataMapper(CharacterMapper()))
    }
}