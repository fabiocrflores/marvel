package com.marvel.developer.persistence

import com.marvel.developer.persistence.infrastructures.FavoriteCacheInfrastructureTest
import com.marvel.developer.persistence.mappers.FavoriteCharacterMapperTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    FavoriteCacheInfrastructureTest::class,
    FavoriteCharacterMapperTest::class
)
class UnitTestSuite