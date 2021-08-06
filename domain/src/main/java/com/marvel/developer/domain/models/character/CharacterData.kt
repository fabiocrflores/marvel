package com.marvel.developer.domain.models.character

data class CharacterData(
    val offset: Int,
    val total: Int,
    val characters: List<Character>
)