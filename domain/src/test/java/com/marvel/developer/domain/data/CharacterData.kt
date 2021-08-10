package com.marvel.developer.domain.data

import com.marvel.developer.domain.models.character.Character
import com.marvel.developer.domain.models.character.CharacterData
import com.marvel.developer.domain.models.character.CharacterResult

val characterResult by lazy {
    CharacterResult(characterData)
}

val characterData by lazy {
    CharacterData(offset = 0, total = 1504, characters = mutableListOf(character, character1))
}

val character by lazy {
    Character(
        id = 1011334,
        name = "3-D Man",
        description = "",
        imageUrl = "http://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784.jpg",
        isFavorite = false
    )
}

val character1 by lazy {
    Character(
        id = 1017100,
        name = "A-Bomb (HAS)",
        description = "Rick Jones has been Hulk's best bud since day one, but now he's more than a friend...he's a teammate! Transformed by a Gamma energy explosion, A-Bomb's thick, armored skin is just as strong and powerful as it is blue. And when he curls into action, he uses it like a giant bowling ball of destruction! ",
        imageUrl = "http://i.annihil.us/u/prod/marvel/i/mg/3/20/5232158de5b16.jpg",
        isFavorite = true
    )
}
