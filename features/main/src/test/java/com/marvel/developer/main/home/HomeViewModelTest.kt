package com.marvel.developer.main.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingSource
import com.marvel.developer.domain.models.character.Character
import com.marvel.developer.domain.repositories.CharacterRepository
import com.marvel.developer.main.data.character
import com.marvel.developer.main.data.characterResult
import com.marvel.developer.main.data.favoriteCharacter
import com.marvel.developer.main.home.paging.CharacterDataSource
import com.marvel.developer.main.rules.MainCoroutineRule
import com.marvel.developer.main.rules.RxSchedulerRule
import com.marvel.developer.shareutils.StateMachine
import com.marvel.developer.shareutils.ViewState
import com.nhaarman.mockitokotlin2.*
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions
import org.junit.*

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var testSchedulerRule = RxSchedulerRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: HomeViewModel
    private val mockRepository = mock<CharacterRepository>()
    private lateinit var characterDataSource: CharacterDataSource
    private val timesInvocation = 1

    @Before
    fun `before each test`() {
        viewModel = HomeViewModel(
            repository = mockRepository,
            machine = StateMachine(),
            stateHandle = SavedStateHandle()
        )

        characterDataSource = CharacterDataSource(
            repository = mockRepository,
            nameByFilter = null,
            favoriteCharacters = emptyList()
        )
    }

    @After
    fun `after each test`() {
        viewModel.favoriteCharacters.clear()
    }

    @Test
    fun `should emmit success states for repository and return favorite characters cached`() {
        val expected = listOf(character)

        whenever(mockRepository.fetchFavoriteCharactersCached())
            .thenReturn(Flowable.just(listOf(character)))

        viewModel.fetchFavoriteCharactersCached().test()
            .assertComplete()
            .assertValueSequence(
                listOf(
                    ViewState.Launched,
                    ViewState.Success(expected),
                    ViewState.Done
                )
            )

        verify(mockRepository, times(timesInvocation)).fetchFavoriteCharactersCached()
    }


    @Test
    fun `should return characters`() {
        mainCoroutineRule.runBlockingTest {
            whenever(mockRepository.fetchCharacters(null, 0))
                .thenReturn(Single.just(characterResult))

            val expected = PagingSource.LoadResult.Page(
                data = characterResult.data.characters,
                prevKey = null,
                nextKey = 20
            )

            Assert.assertEquals(
                expected, characterDataSource.load(
                    PagingSource.LoadParams.Refresh(
                        key = 0,
                        loadSize = 1,
                        placeholdersEnabled = false
                    )
                )
            )

            verify(mockRepository, times(timesInvocation)).fetchCharacters(null, 0)
        }
    }

    @Test
    fun `should return error when 404`() {
        mainCoroutineRule.runBlockingTest {
            val error = RuntimeException("404", Throwable())
            whenever(mockRepository.fetchCharacters(null, 0))
                .thenReturn(Single.error(error))

            val expected = PagingSource.LoadResult.Error<Int, Character>(error)

            Assert.assertEquals(
                expected, characterDataSource.load(
                    PagingSource.LoadParams.Refresh(
                        key = 0,
                        loadSize = 1,
                        placeholdersEnabled = false
                    )
                )
            )

            verify(mockRepository, times(timesInvocation)).fetchCharacters(null, 0)
        }
    }

    @Test
    fun `should save favorite character cached`() {
        favoriteCharacter.isFavorite = true

        viewModel.favoriteCharacters.add(favoriteCharacter)
        viewModel.setFavoriteCharacterCache(character)

        Assertions.assertThat(viewModel.favoriteCharacters.size).isEqualTo(2)
        verify(mockRepository, times(timesInvocation)).saveFavoriteCharacterCache(any())
        verify(mockRepository, never()).deleteFavoriteCharacterCached(any())
    }

    @Test
    fun `should delete favorite character cached`() {
        favoriteCharacter.isFavorite = true

        viewModel.favoriteCharacters.addAll(mutableListOf(character, favoriteCharacter))
        viewModel.setFavoriteCharacterCache(favoriteCharacter)

        Assertions.assertThat(viewModel.favoriteCharacters.size).isEqualTo(1)
        verify(mockRepository, never()).saveFavoriteCharacterCache(any())
        verify(mockRepository, times(timesInvocation)).deleteFavoriteCharacterCached(any())
    }
}