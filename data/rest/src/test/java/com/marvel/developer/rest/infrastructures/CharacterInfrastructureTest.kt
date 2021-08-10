package com.marvel.developer.rest.infrastructures

import com.marvel.developer.domain.errors.RemoteServiceIntegrationError
import com.marvel.developer.domain.models.character.CharacterResult
import com.marvel.developer.rest.errors.ExecutionErrorHandler
import com.marvel.developer.rest.mappers.character.CharacterDataMapper
import com.marvel.developer.rest.mappers.character.CharacterMapper
import com.marvel.developer.rest.mappers.character.CharacterResultMapper
import com.marvel.developer.rest.data.characterResult
import com.marvel.developer.rest.utils.InfrastructureRule
import com.marvel.developer.rest.utils.loadFiles
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subscribers.TestSubscriber
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class CharacterInfrastructureTest {

    @get:Rule
    val rule = InfrastructureRule()

    private lateinit var infrastructure: CharacterInfrastructure

    @Before
    fun `before each test`() {
        infrastructure = CharacterInfrastructure(
            service = rule.marvelAPI,
            mapper = CharacterResultMapper(
                CharacterDataMapper(CharacterMapper()),
            ),
            errorHandler = ExecutionErrorHandler(),
            targetScheduler = Schedulers.trampoline()
        )
    }

    @Test
    fun `should handle result on fetch characters`() {
        rule.defineScenario(
            status = 200,
            response = loadFiles("character/200_character_with_result.json")
        )

        val testSubscriber = TestSubscriber<CharacterResult>()
        simpleFetchCharacter().subscribe(testSubscriber)

        testSubscriber
            .await()
            .assertNoErrors()
            .assertValue(characterResult)
    }

    @Test
    fun `should handle 200 on fetch characters but broken contract`() {
        rule.defineScenario(
            status = 200,
            response = loadFiles("character/200_character_broken_contract.json")
        )

        val testSubscriber = TestSubscriber<CharacterResult>()
        simpleFetchCharacter().subscribe(testSubscriber)

        testSubscriber.await()
            .assertError(RemoteServiceIntegrationError.UnexpectedResponse)
    }

    @Test
    fun `should handle 404 not found on fetch characters`() {
        rule.defineScenario(status = 404)

        val testSubscriber = TestSubscriber<CharacterResult>()
        simpleFetchCharacter().subscribe(testSubscriber)

        testSubscriber.await()
            .assertError(RemoteServiceIntegrationError.ClientOrigin)
    }

    @Test
    fun `should handle 503 internal server error on fetch characters`() {
        rule.defineScenario(status = 503)

        val testSubscriber = TestSubscriber<CharacterResult>()
        simpleFetchCharacter().subscribe(testSubscriber)

        testSubscriber.await()
            .assertError(RemoteServiceIntegrationError.RemoteSystem)
    }

    private fun simpleFetchCharacter() =
        infrastructure.fetchCharacters(null, 0).toFlowable()
}
