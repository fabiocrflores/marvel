package com.marvel.developer.domain.services

import com.marvel.developer.domain.models.character.CharacterResult
import io.reactivex.rxjava3.core.Single

interface CharacterService {
    fun fetchCharacters(nameByFilter: String?, offset: Int): Single<CharacterResult>
}