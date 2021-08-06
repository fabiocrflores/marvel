package com.marvel.developer.shareutils.di

import com.marvel.developer.shareutils.Actions
import com.marvel.developer.shareutils.Disposer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ShareUtilsModule {

    @Provides
    fun provideActions() = Actions()

    @Provides
    fun provideDisposer(): Disposer = Disposer()
}