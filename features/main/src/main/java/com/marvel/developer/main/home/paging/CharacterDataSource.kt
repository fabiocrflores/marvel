package com.marvel.developer.main.home.paging

import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.marvel.developer.domain.models.character.Character
import com.marvel.developer.domain.models.character.CharacterResult
import com.marvel.developer.domain.repositories.CharacterRepository
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Single
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CharacterDataSource @Inject constructor(
    private val repository: CharacterRepository,
    private val nameByFilter: String?
) : RxPagingSource<Int, Character>() {

    companion object {
        private const val OFFSET_INITIAL = 0
        private const val PAGE_SIZE = 20
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Character>> {
        val offset = params.key ?: OFFSET_INITIAL

        return repository.fetchCharacters(nameByFilter = nameByFilter, offset = offset)
            .map(this::toLoadResult)
            .onErrorReturn { e -> LoadResult.Error(e) }
    }

    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        return state.anchorPosition?.let { state.closestItemToPosition(it)?.id }
    }

    private fun toLoadResult(
        @NonNull response: CharacterResult
    ): LoadResult<Int, Character> {
        val offset = response.data.offset
        val total = response.data.total

        return LoadResult.Page(
            data = response.data.characters,
            prevKey = if (offset == OFFSET_INITIAL) null else offset - PAGE_SIZE,
            nextKey = if (offset >= total) null else offset + PAGE_SIZE
        )
    }
}