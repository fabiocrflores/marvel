package com.marvel.developer.domain.repositories

import com.marvel.developer.domain.models.character.Character
import com.marvel.developer.domain.models.character.CharacterResult
import com.marvel.developer.domain.services.CharacterService
import com.marvel.developer.domain.services.FavoriteCharacterCacheService
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

class CharacterRepository(
    private val service: CharacterService,
    private val cacheService: FavoriteCharacterCacheService
) {

    fun fetchCharacters(nameByFilter: String?, offset: Int): Single<CharacterResult> =
        service.fetchCharacters(nameByFilter = nameByFilter, offset = offset)

    fun saveFavoriteCharacterCache(model: Character) {
        cacheService.saveCache(model)
    }

    fun fetchFavoriteCharactersCached(): Flowable<List<Character>> =
        cacheService.fetchAllCached()

    fun deleteFavoriteCharacterCached(model: Character) {
        cacheService.deleteCached(model)
    }
}