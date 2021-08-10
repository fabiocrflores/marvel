package com.marvel.developer.persistence.mappers

import com.marvel.developer.persistence.data.character
import com.marvel.developer.persistence.data.favoriteCharacterEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class FavoriteCharacterMapperTest {

    private lateinit var mapper: FavoriteCharacterMapper

    @Before
    fun `before each test`() {
        mapper = FavoriteCharacterMapper()
    }

    @Test
    fun `should map model to entity`() {
        val expected = favoriteCharacterEntity

        assertThat(mapper.fromModel(character)).isEqualTo(expected)
    }

    @Test
    fun `should map entity to model`() {
        val expected = character

        assertThat(mapper.fromEntity(favoriteCharacterEntity)).isEqualTo(expected)
    }
}