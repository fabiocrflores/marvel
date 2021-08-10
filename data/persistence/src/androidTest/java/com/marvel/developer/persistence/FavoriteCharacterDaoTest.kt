package com.marvel.developer.persistence

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.marvel.developer.persistence.data.favoriteCharacterEntity
import com.marvel.developer.persistence.room.MarvelDatabase
import com.marvel.developer.persistence.room.dao.FavoriteCharacterDao
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavoriteCharacterDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var roomDatabase: MarvelDatabase
    private lateinit var dao: FavoriteCharacterDao

    @Before
    fun initDb() {
        roomDatabase = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            MarvelDatabase::class.java
        ).build()

        dao = roomDatabase.favoriteCharacterDao()
    }

    @After
    fun closeDb() {
        roomDatabase.close()
    }

    @Test
    fun saveAndLoadFavoritesTest() {
        val expected = listOf(favoriteCharacterEntity)

        dao.save(favoriteCharacterEntity)

        dao.loadAll()
            .test()
            .assertValue(expected)
    }

    @Test
    fun loadByIdTest() {
        val expected = favoriteCharacterEntity

        dao.save(favoriteCharacterEntity)

        dao.loadById(id = 1)
            .test()
            .assertValue(expected)
    }

    @Test
    fun deleteEventLogsTest() {
        dao.save(favoriteCharacterEntity)

        dao.delete(favoriteCharacterEntity)

        dao.loadAll()
            .test()
            .assertValue { list -> list.isEmpty() }
    }
}