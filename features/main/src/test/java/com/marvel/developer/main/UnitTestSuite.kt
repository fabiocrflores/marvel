package com.marvel.developer.main

import com.marvel.developer.main.favorites.FavoritesViewModelTest
import com.marvel.developer.main.home.HomeViewModelTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.runner.RunWith
import org.junit.runners.Suite

@ExperimentalCoroutinesApi
@RunWith(Suite::class)
@Suite.SuiteClasses(
    FavoritesViewModelTest::class,
    HomeViewModelTest::class
)
class UnitTestSuite