package com.marvel.developer.rest.responses.character

import com.google.gson.annotations.SerializedName
import com.marvel.developer.rest.responses.ThumbnailResponse

data class CharacterResponse(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String,
        @SerializedName("description") val description: String,
        @SerializedName("thumbnail") val thumbnail: ThumbnailResponse?
)
