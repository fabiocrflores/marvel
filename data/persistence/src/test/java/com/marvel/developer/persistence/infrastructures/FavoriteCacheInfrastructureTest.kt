package com.marvel.developer.persistence.infrastructures

import com.marvel.developer.persistence.data.character
import com.marvel.developer.persistence.data.favoriteCharacterEntity
import com.marvel.developer.persistence.mappers.FavoriteCharacterMapper
import com.marvel.developer.persistence.room.dao.FavoriteCharacterDao
import com.nhaarman.mockitokotlin2.*
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Before
import org.junit.Test

class FavoriteCacheInfrastructureTest {

    private val mockDao = mock<FavoriteCharacterDao>()
    private lateinit var cache: FavoriteCharacterCacheInfrastructure
    private val timesInvocation = 1

    @Before
    fun `before each test`() {
        cache = FavoriteCharacterCacheInfrastructure(
            dao = mockDao,
            targetScheduler = Schedulers.io(),
            mapper = FavoriteCharacterMapper()
        )
    }

    @Test
    fun `should save favorite character to cache when provided`() {
        cache.saveCache(character)

        verify(mockDao, times(timesInvocation)).save(any())
    }

    @Test
    fun `should return favorite characters cached`() {
        val expected = listOf(character)

        whenever(mockDao.loadAll())
            .thenReturn(Maybe.just(listOf(favoriteCharacterEntity)))

        val testObserver = cache.fetchAllCached().test()
        testObserver.await()
        testObserver.assertComplete()
        testObserver.assertValue(expected)

        verify(mockDao, times(timesInvocation)).loadAll()
    }

    @Test
    fun `should delete favorite cached`() {
        cache.deleteCached(character)

        verify(mockDao, times(timesInvocation)).delete(any())
    }
}