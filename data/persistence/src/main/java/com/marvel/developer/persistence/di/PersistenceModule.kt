package com.marvel.developer.persistence.di

import android.app.Application
import androidx.room.Room
import com.marvel.developer.persistence.infrastructures.FavoriteCharacterCacheInfrastructure
import com.marvel.developer.persistence.mappers.FavoriteCharacterMapper
import com.marvel.developer.persistence.room.MarvelDatabase
import com.marvel.developer.persistence.room.dao.FavoriteCharacterDao
import com.marvel.developer.domain.services.FavoriteCharacterCacheService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PersistenceModule {

    @Binds
    @Singleton
    abstract fun bindFavoriteCharacterCacheService(
        infrastructure: FavoriteCharacterCacheInfrastructure
    ): FavoriteCharacterCacheService

    companion object {

        @Provides
        @Singleton
        fun provideAgentPolarRoomDatabase(app: Application): MarvelDatabase =
            Room.databaseBuilder(
                app,
                MarvelDatabase::class.java,
                MarvelDatabase.DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()

        @Provides
        @Singleton
        fun provideFavoriteCharacterDao(appDatabase: MarvelDatabase): FavoriteCharacterDao =
            appDatabase.favoriteCharacterDao()

        @Provides
        @Singleton
        fun provideFavoriteCharacterMapper(): FavoriteCharacterMapper = FavoriteCharacterMapper()
    }
}