package com.marvel.developer.rest.api

import com.marvel.developer.rest.responses.character.CharacterResultResponse
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MarvelAPI {

    @GET("v1/public/characters")
    fun fetchCharacters(
        @Query("nameStartsWith") nameStartsWith: String?,
        @Query("offset") offset: Int
    ): Single<CharacterResultResponse>
}