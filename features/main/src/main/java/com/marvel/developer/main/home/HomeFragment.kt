package com.marvel.developer.main.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.*
import com.marvel.developer.domain.models.character.Character
import com.marvel.developer.main.R
import com.marvel.developer.main.databinding.FragmentHomeBinding
import com.marvel.developer.main.databinding.StateCharacterContentBinding
import com.marvel.developer.main.home.adapters.CharacterAdapter
import com.marvel.developer.shareutils.ViewState
import com.marvel.developer.shareutils.bases.fragment.BaseFragmentDataBinding
import com.marvel.developer.shareutils.constants.App.FAVORITE_RESULT_KEY_VALUE
import com.marvel.developer.shareutils.constants.App.HOME_REQUEST_KEY_VALUE
import com.marvel.developer.shareutils.databinding.StateErrorLoadDataBinding
import com.marvel.developer.shareutils.widgets.manyfacedview.view.FacedViewState.Companion.CONTENT
import com.marvel.developer.shareutils.widgets.manyfacedview.view.FacedViewState.Companion.EMPTY
import com.marvel.developer.shareutils.widgets.manyfacedview.view.FacedViewState.Companion.ERROR
import com.marvel.developer.shareutils.widgets.manyfacedview.view.FacedViewState.Companion.LOADING
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber
import java.util.concurrent.TimeUnit


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeFragment : BaseFragmentDataBinding<FragmentHomeBinding>(
    R.layout.fragment_home
) {

    companion object {
        private const val DEBOUNCE_TIME = 750L
    }

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var bindingContent: StateCharacterContentBinding

    private lateinit var characterAdapter: CharacterAdapter
    private val searchSubject = PublishSubject.create<String>()
    private var numberCharactersSearch: Int = 3

    @Suppress("UNCHECKED_CAST")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycle.addObserver(disposer)

        binding.vm = viewModel

        val viewContent = binding.stateView.getView(CONTENT) as View
        bindingContent = StateCharacterContentBinding.bind(viewContent)

        setupToolbar()
        setupViews()
        setupEditors()
        setupSubject()

        viewModel.characters.value?.let {
            handleSuccess(it)
        } ?: fetchCharacters()

        setFragmentResultListener(HOME_REQUEST_KEY_VALUE) { _, bundle ->
            if (bundle.containsKey(FAVORITE_RESULT_KEY_VALUE)) {
                val listToUpdate = bundle.get(FAVORITE_RESULT_KEY_VALUE) as MutableList<Character>
                listToUpdate.forEach {
                    characterAdapter.updateFavorite(it)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.home_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.favorites) {
            goToFavorites()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setupToolbar() {
        (mActivity as AppCompatActivity).apply {
            setSupportActionBar(binding.includeCustomToolbar.customToolbar)
            supportActionBar?.title = ""
            supportActionBar?.setDisplayHomeAsUpEnabled(false)

            binding.includeCustomToolbar.textToolbarTitle.text = getString(R.string.characters)
        }
        setHasOptionsMenu(true)
    }

    private fun setupViews() {
        characterAdapter = CharacterAdapter(
            clickAction = ::goToDetails,
            favoriteClickAction = ::setFavorite
        )
        characterAdapter.addLoadStateListener { loadState ->
            val errorState = loadState.refresh as? LoadState.Error
                ?: loadState.append as? LoadState.Error
            errorState?.let {
                handleError(it.error)
            } ?: run {
                val isLoading = loadState.source.refresh is LoadState.Loading
                binding.stateView.setState(
                    if (isLoading || !loadState.source.prepend.endOfPaginationReached) {
                        LOADING
                    } else {
                        if (characterAdapter.itemCount == 0) EMPTY else CONTENT
                    }
                )
            }

            val isLoadingAppend = loadState.append is LoadState.Loading
            bindingContent.progressBarFooter.visibility = if (isLoadingAppend) VISIBLE else GONE
        }

        bindingContent.charactersList.apply {
            setHasFixedSize(true)
            adapter = characterAdapter
        }
    }

    private fun setupEditors() {
        binding.searchCharacter.apply {
            viewModel.nameByFilter.value?.let {
                if (it.isNotEmpty()) setQuery(it, false)
            }

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    searchSubject.onNext(query.toString())

                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText != viewModel.nameByFilter.value) {
                        viewModel.nameByFilter.value = newText

                        if (newText.isNullOrEmpty()) {
                            searchSubject.onNext("")
                        } else {
                            if (newText.length < 3) {
                                numberCharactersSearch = 3
                            } else if (newText.length == numberCharactersSearch) {
                                searchSubject.onNext(newText.toString())
                                numberCharactersSearch += 3

                                return true
                            }
                        }
                    }

                    return false
                }
            })
        }
    }

    private fun setupSubject() {
        val toDispose = searchSubject
            .debounce(DEBOUNCE_TIME, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .subscribe {
                fetchCharacters()
            }

        disposer.collect(toDispose)
    }

    private fun fetchCharacters() {
        val toDispose = viewModel.fetchCharacters()
            .subscribe(
                { changeState(it) },
                { handleError(it) }
            )

        disposer.collect(toDispose)
    }

    private fun changeState(event: ViewState<PagingData<Character>>) {
        when (event) {
            is ViewState.Launched -> startExecution()
            is ViewState.Success -> handleSuccess(event.value)
            is ViewState.Failed -> handleError(event.reason)
            is ViewState.Done -> Unit
        }
    }

    private fun startExecution() {
        binding.stateView.setState(LOADING)
    }

    private fun handleSuccess(
        presentation: PagingData<Character>
    ) {
        Timber.d("$presentation")

        viewModel.characters.value = presentation
        characterAdapter.submitData(lifecycle, presentation)
    }

    private fun handleError(reason: Throwable) {
        Timber.e("$reason")

        binding.stateView.setState(ERROR)

        val viewError = binding.stateView.getView(ERROR) as View
        val bindingError = StateErrorLoadDataBinding.bind(viewError)
        bindingError.buttonTryAgain.apply {
            setOnClickListener {
                characterAdapter.retry()
            }
        }
    }

    private fun setFavorite(position: Int, character: Character) {
        viewModel.setFavoriteCharacterCache(character)
        characterAdapter.notifyItemChanged(position)
    }

    private fun goToDetails(character: Character) {
        val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(character)
        findNavController().navigate(action)
    }

    private fun goToFavorites() {
        val action = HomeFragmentDirections.actionHomeFragmentToFavoritesFragment()
        findNavController().navigate(action)
    }
}