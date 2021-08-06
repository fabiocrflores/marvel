package com.marvel.developer.domain.di

import com.marvel.developer.domain.repositories.CharacterRepository
import com.marvel.developer.domain.services.CharacterService
import com.marvel.developer.domain.services.FavoriteCharacterCacheService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    @Singleton
    fun provideCharacterRepository(
        service: CharacterService,
        cacheService: FavoriteCharacterCacheService
    ): CharacterRepository = CharacterRepository(service = service, cacheService = cacheService)
}