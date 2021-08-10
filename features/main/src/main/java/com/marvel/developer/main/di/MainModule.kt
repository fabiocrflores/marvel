package com.marvel.developer.main.di

import com.marvel.developer.domain.models.character.Character
import com.marvel.developer.shareutils.StateMachine
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Singleton
    @Provides
    fun provideCharactersStateMachine(): StateMachine<List<Character>> = StateMachine()
}