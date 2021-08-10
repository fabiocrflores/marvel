package com.marvel.developer.domain

import com.marvel.developer.domain.errors.CommonErrorTest
import com.marvel.developer.domain.errors.NetworkingErrorTest
import com.marvel.developer.domain.errors.RemoteServiceIntegrationErrorTest
import com.marvel.developer.domain.repositories.CharacterRepositoryTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    CommonErrorTest::class,
    NetworkingErrorTest::class,
    RemoteServiceIntegrationErrorTest::class,
    CharacterRepositoryTest::class
)
class UnitTestSuite