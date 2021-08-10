package com.marvel.developer.persistence.data

import com.marvel.developer.domain.models.character.Character
import com.marvel.developer.persistence.entities.FavoriteCharacterEntity

val character by lazy {
    Character(
        id = 1,
        name = "3-D Man",
        description = "",
        imageUrl = "http://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784.jpg",
        isFavorite = true
    )
}

val favoriteCharacterEntity by lazy {
    FavoriteCharacterEntity(
        id = 1,
        name = "3-D Man",
        description = "",
        imageUrl = "http://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784.jpg",
    )
}