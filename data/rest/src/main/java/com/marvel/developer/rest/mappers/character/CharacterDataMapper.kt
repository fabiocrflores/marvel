package com.marvel.developer.rest.mappers.character

import com.marvel.developer.domain.models.character.CharacterData
import com.marvel.developer.rest.responses.character.CharacterDataResponse

class CharacterDataMapper(
    private val characterMapper: CharacterMapper
) {

    fun fromResponse(response: CharacterDataResponse): CharacterData = with(response) {
        CharacterData(
            offset = offset,
            total = total,
            characters = characters.map { characterMapper.fromResponse(it) }
        )
    }
}