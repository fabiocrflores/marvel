package com.marvel.developer.rest.mappers

import com.marvel.developer.rest.mappers.character.CharacterMapper
import com.marvel.developer.rest.data.characterResponse
import com.marvel.developer.rest.data.character
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class CharacterMapperTest {

    private lateinit var mapper: CharacterMapper

    @Before
    fun `before each test`() {
        mapper = CharacterMapper()
    }

    @Test
    fun `should map device from response`() {
        val response = characterResponse
        val expected = character

        assertThat(mapper.fromResponse(response)).isEqualTo(expected)
    }
}