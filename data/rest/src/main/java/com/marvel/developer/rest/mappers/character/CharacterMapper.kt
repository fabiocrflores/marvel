package com.marvel.developer.rest.mappers.character

import com.marvel.developer.domain.models.character.Character
import com.marvel.developer.rest.responses.character.CharacterResponse

class CharacterMapper {
    fun fromResponse(response: CharacterResponse): Character = with(response) {
        Character(
            id = id,
            name = name,
            description = description,
            imageUrl = thumbnail?.let { "${it.path}.${it.extension}" }
        )
    }
}