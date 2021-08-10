package com.marvel.developer.main.favorites

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.marvel.developer.main.data.character
import com.marvel.developer.main.data.favoriteCharacter
import com.marvel.developer.domain.repositories.CharacterRepository
import com.marvel.developer.main.rules.RxSchedulerRule
import com.marvel.developer.shareutils.StateMachine
import com.marvel.developer.shareutils.ViewState
import com.nhaarman.mockitokotlin2.*
import io.reactivex.rxjava3.core.Flowable
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FavoritesViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var testSchedulerRule = RxSchedulerRule()

    private lateinit var viewModel: FavoritesViewModel
    private val mockRepository = mock<CharacterRepository>()
    private val timesInvocation = 1

    @Before
    fun `before each test`() {
        viewModel = FavoritesViewModel(repository = mockRepository, machine = StateMachine())
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
    fun `should delete favorite character cached`() {
        viewModel.characters.addAll(mutableListOf(character, favoriteCharacter))
        viewModel.deleteFavoriteCharacterCached(character)

        assertThat(viewModel.characters.size).isEqualTo(1)
        verify(mockRepository, times(timesInvocation)).deleteFavoriteCharacterCached(any())
    }
}