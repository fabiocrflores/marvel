package com.marvel.developer.domain.models.character

import java.io.Serializable

data class Character(
    val id: Int,
    val name: String,
    val description: String,
    val imageUrl: String?,
    var isFavorite: Boolean = false
) : Serializable
