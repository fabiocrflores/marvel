package com.marvel.developer.main.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.cachedIn
import androidx.paging.rxjava3.flowable
import com.marvel.developer.domain.models.character.Character
import com.marvel.developer.domain.repositories.CharacterRepository
import com.marvel.developer.main.home.paging.CharacterDataSource
import com.marvel.developer.shareutils.StateMachine
import com.marvel.developer.shareutils.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Flowable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: CharacterRepository,
    private val machineSchedules: StateMachine<PagingData<Character>>,
    private val stateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val NAME_BY_FILTER_KEY_VALUE = "key.name.by.filter.value"
    }

    var nameByFilter: MutableLiveData<String> = stateHandle.getLiveData(
        NAME_BY_FILTER_KEY_VALUE,
        ""
    )
        set(value) {
            field = value
            stateHandle.set(NAME_BY_FILTER_KEY_VALUE, value)
        }

    val characters = MutableLiveData<PagingData<Character>>(null)

    @ExperimentalCoroutinesApi
    fun fetchCharacters(): Flowable<ViewState<PagingData<Character>>> =
        Pager(config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = {
                CharacterDataSource(
                    repository = repository,
                    nameByFilter = if (nameByFilter.value!!.isEmpty()) null else nameByFilter.value
                )
            }
        )
            .flowable.cachedIn(viewModelScope)
            .compose(machineSchedules)


    fun setFavoriteCharacterCache(model: Character) {
        model.isFavorite = !model.isFavorite
        if (model.isFavorite) {
            repository.saveFavoriteCharacterCache(model)
        } else {
            repository.deleteFavoriteCharacterCached(model)
        }
    }
}