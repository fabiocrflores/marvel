package com.marvel.developer.rest.mappers

import com.marvel.developer.rest.mappers.character.CharacterDataMapper
import com.marvel.developer.rest.mappers.character.CharacterMapper
import com.marvel.developer.rest.mappers.character.CharacterResultMapper
import com.marvel.developer.rest.data.characterResult
import com.marvel.developer.rest.data.characterResultResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class CharacterResultTest {

    private lateinit var mapper: CharacterResultMapper

    @Before
    fun `before each test`() {
        mapper = CharacterResultMapper(CharacterDataMapper(CharacterMapper()))
    }

    @Test
    fun `should map device from response`() {
        val response = characterResultResponse
        val expected = characterResult

        assertThat(mapper.fromResponse(response)).isEqualTo(expected)
    }
}