package com.marvel.developer.rest.responses.character

import com.google.gson.annotations.SerializedName

data class CharacterDataResponse(
    @SerializedName("offset") val offset: Int,
    @SerializedName("limit") val limit: Int,
    @SerializedName("total") val total: Int,
    @SerializedName("count") val count: Int,
    @SerializedName("results") val characters: List<CharacterResponse>
)