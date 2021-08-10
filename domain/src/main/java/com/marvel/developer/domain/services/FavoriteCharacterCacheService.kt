package com.marvel.developer.domain.services

import com.marvel.developer.domain.models.character.Character
import io.reactivex.rxjava3.core.Flowable

interface FavoriteCharacterCacheService {
    fun saveCache(model: Character)
    fun fetchAllCached(): Flowable<List<Character>>
    fun deleteCached(model: Character)
}