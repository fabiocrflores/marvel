package com.marvel.developer.rest.mappers.character

import com.marvel.developer.domain.models.character.CharacterResult
import com.marvel.developer.rest.responses.character.CharacterResultResponse

class CharacterResultMapper(
    private val characterDataMapper: CharacterDataMapper
) {

    fun fromResponse(response: CharacterResultResponse): CharacterResult = with(response) {
        CharacterResult(characterDataMapper.fromResponse(data))
    }
}