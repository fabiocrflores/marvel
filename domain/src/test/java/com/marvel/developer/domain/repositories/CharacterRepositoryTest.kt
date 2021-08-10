package com.marvel.developer.domain.repositories

import com.marvel.developer.domain.data.character
import com.marvel.developer.domain.data.characterResult
import com.marvel.developer.domain.services.CharacterService
import com.marvel.developer.domain.services.FavoriteCharacterCacheService
import com.nhaarman.mockitokotlin2.*
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Test

class CharacterRepositoryTest {

    private val mockService = mock<CharacterService>()
    private val mockCacheService = mock<FavoriteCharacterCacheService>()
    private lateinit var repository: CharacterRepository
    private val timesInvocation = 1

    @Before
    fun `before each test`() {
        repository = CharacterRepository(service = mockService, cacheService = mockCacheService)
    }

    @Test
    fun `should return characters from service`() {
        whenever(mockService.fetchCharacters(nameByFilter = null, offset = 20))
            .thenReturn(Single.just(characterResult))

        repository.fetchCharacters(nameByFilter = null, offset = 20).test()
            .await()
            .assertValue(characterResult)

        verify(mockService, times(timesInvocation)).fetchCharacters(null, 20)
    }

    @Test
    fun `should save favorite character on cache`() {
        repository.saveFavoriteCharacterCache(character)

        verify(mockCacheService, times(timesInvocation)).saveCache(any())
    }

    @Test
    fun `should return favorite characters from cache`() {
        whenever(mockCacheService.fetchAllCached())
            .thenReturn(Flowable.just(listOf(character)))

        repository.fetchFavoriteCharactersCached().test()
            .assertComplete()
            .assertValue(listOf(character))

        verify(mockCacheService, times(timesInvocation)).fetchAllCached()
    }

    @Test
    fun `should delete favorite character on cache`() {
        repository.deleteFavoriteCharacterCached(character)

        verify(mockCacheService, times(timesInvocation)).deleteCached(any())
    }
}