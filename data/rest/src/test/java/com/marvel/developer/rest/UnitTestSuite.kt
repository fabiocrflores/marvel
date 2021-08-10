package com.marvel.developer.rest

import com.marvel.developer.rest.infrastructures.CharacterInfrastructureTest
import com.marvel.developer.rest.mappers.CharacterDataMapperTest
import com.marvel.developer.rest.mappers.CharacterMapperTest
import com.marvel.developer.rest.mappers.CharacterResultTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    CharacterInfrastructureTest::class,
    CharacterDataMapperTest::class,
    CharacterMapperTest::class,
    CharacterResultTest::class
)
class UnitTestSuite