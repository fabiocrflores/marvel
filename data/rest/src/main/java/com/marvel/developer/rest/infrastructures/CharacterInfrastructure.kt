package com.marvel.developer.rest.infrastructures

import com.marvel.developer.domain.models.character.CharacterResult
import com.marvel.developer.domain.services.CharacterService
import com.marvel.developer.rest.api.MarvelAPI
import com.marvel.developer.rest.errors.ExecutionErrorHandler
import com.marvel.developer.rest.mappers.character.CharacterResultMapper
import com.marvel.developer.rest.responses.character.CharacterResultResponse
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class CharacterInfrastructure @Inject constructor(
    private val service: MarvelAPI,
    private val mapper: CharacterResultMapper,
    private val errorHandler: ExecutionErrorHandler<CharacterResultResponse>,
    private val targetScheduler: Scheduler
) : CharacterService {

    override fun fetchCharacters(nameByFilter: String?, offset: Int): Single<CharacterResult> =
        service
            .fetchCharacters(nameStartsWith = nameByFilter, offset = offset)
            .subscribeOn(targetScheduler)
            .compose(errorHandler)
            .map { mapper.fromResponse(it) }
}

