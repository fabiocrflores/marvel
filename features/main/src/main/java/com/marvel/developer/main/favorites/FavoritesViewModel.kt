package com.marvel.developer.main.favorites

import androidx.lifecycle.ViewModel
import com.marvel.developer.domain.models.character.Character
import com.marvel.developer.domain.repositories.CharacterRepository
import com.marvel.developer.shareutils.StateMachine
import com.marvel.developer.shareutils.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: CharacterRepository,
    private val machine: StateMachine<List<Character>>
) : ViewModel() {

    val characters = mutableListOf<Character>()
    val deletedFavorites = mutableListOf<Character>()

    fun fetchFavoriteCharactersCached(): Flowable<ViewState<List<Character>>> =
        repository.fetchFavoriteCharactersCached()
            .compose(machine)

    fun deleteFavoriteCharacterCached(model: Character) {
        repository.deleteFavoriteCharacterCached(model)
        characters.remove(model)

        deletedFavorites.add(model)
    }
}