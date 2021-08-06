package com.marvel.developer.rest.responses.character

import com.google.gson.annotations.SerializedName
import com.marvel.developer.rest.responses.character.CharacterDataResponse

data class CharacterResultResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("status") val status: String,
    @SerializedName("data") val data: CharacterDataResponse
)