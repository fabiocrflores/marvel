package com.marvel.developer.persistence.mappers

import com.marvel.developer.domain.models.character.Character
import com.marvel.developer.persistence.entities.FavoriteCharacterEntity

class FavoriteCharacterMapper {

    fun fromModel(model: Character): FavoriteCharacterEntity = with(model) {
        FavoriteCharacterEntity(
            id = id,
            name = name,
            description = description,
            imageUrl = imageUrl
        )
    }

    fun fromEntity(entity: FavoriteCharacterEntity): Character = with(entity) {
        Character(
            id = id,
            name = name,
            description = description,
            imageUrl = imageUrl,
            isFavorite = true
        )
    }
}