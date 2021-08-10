package com.marvel.developer.rest.data

import com.marvel.developer.domain.models.character.Character
import com.marvel.developer.domain.models.character.CharacterData
import com.marvel.developer.domain.models.character.CharacterResult
import com.marvel.developer.rest.responses.ThumbnailResponse
import com.marvel.developer.rest.responses.character.CharacterDataResponse
import com.marvel.developer.rest.responses.character.CharacterResponse
import com.marvel.developer.rest.responses.character.CharacterResultResponse

val characterResultResponse by lazy {
    CharacterResultResponse(code = 200, status = "OK", data = characterDataResponse)
}

val characterDataResponse by lazy {
    CharacterDataResponse(
        offset = 0,
        limit = 20,
        total = 1504,
        count = 20,
        characters = mutableListOf(characterResponse, characterResponse1)
    )
}

val characterResponse by lazy {
    CharacterResponse(
        id = 1011334,
        name = "3-D Man",
        description = "",
        thumbnail = ThumbnailResponse(
            path = "http://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784",
            extension = "jpg"
        )
    )
}

val characterResponse1 by lazy {
    CharacterResponse(
        id = 1017100,
        name = "A-Bomb (HAS)",
        description = "Rick Jones has been Hulk's best bud since day one, but now he's more than a friend...he's a teammate! Transformed by a Gamma energy explosion, A-Bomb's thick, armored skin is just as strong and powerful as it is blue. And when he curls into action, he uses it like a giant bowling ball of destruction! ",
        thumbnail = ThumbnailResponse(
            path = "http://i.annihil.us/u/prod/marvel/i/mg/3/20/5232158de5b16",
            extension = "jpg"
        )
    )
}

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
        isFavorite = false
    )
}
