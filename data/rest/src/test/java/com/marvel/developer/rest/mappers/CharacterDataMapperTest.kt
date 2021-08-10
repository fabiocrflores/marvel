package com.marvel.developer.rest.mappers

import com.marvel.developer.rest.mappers.character.CharacterDataMapper
import com.marvel.developer.rest.mappers.character.CharacterMapper
import com.marvel.developer.rest.data.characterData
import com.marvel.developer.rest.data.characterDataResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class CharacterDataMapperTest {

    private lateinit var mapper: CharacterDataMapper

    @Before
    fun `before each test`() {
        mapper = CharacterDataMapper(CharacterMapper())
    }

    @Test
    fun `should map device from response`() {
        val response = characterDataResponse
        val expected = characterData

        assertThat(mapper.fromResponse(response)).isEqualTo(expected)
    }
}