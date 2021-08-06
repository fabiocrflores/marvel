package com.marvel.developer.persistence.infrastructures

import com.marvel.developer.domain.models.character.Character
import com.marvel.developer.domain.services.FavoriteCharacterCacheService
import com.marvel.developer.persistence.mappers.FavoriteCharacterMapper
import com.marvel.developer.persistence.room.dao.FavoriteCharacterDao
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

class FavoriteCharacterCacheInfrastructure @Inject constructor(
    private val dao: FavoriteCharacterDao,
    private val targetScheduler: Scheduler,
    private val mapper: FavoriteCharacterMapper
) : FavoriteCharacterCacheService {

    override fun saveCache(model: Character) {
        val entity = mapper.fromModel(model)
        dao.save(entity)
    }

    override fun fetchAllCached(): Flowable<List<Character>> =
        dao.loadAll()
            .subscribeOn(targetScheduler)
            .map { cached -> cached.map { mapper.fromEntity(it) } }
            .toFlowable()

    override fun fetchCachedById(id: Int): Flowable<Character> =
        dao.loadById(id)
            .subscribeOn(targetScheduler)
            .map { mapper.fromEntity(it) }
            .toFlowable()

    override fun deleteCached(model: Character) {
        val entity = mapper.fromModel(model)
        dao.delete(entity)
    }
}