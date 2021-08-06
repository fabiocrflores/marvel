package com.marvel.developer.main.favorites

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.marvel.developer.domain.models.character.Character
import com.marvel.developer.main.R
import com.marvel.developer.main.databinding.FragmentFavoritesBinding
import com.marvel.developer.main.databinding.StateFavoriteContentBinding
import com.marvel.developer.main.favorites.adapters.FavoriteAdapter
import com.marvel.developer.main.home.HomeFragmentDirections
import com.marvel.developer.shareutils.ViewState
import com.marvel.developer.shareutils.bases.fragment.BaseFragmentViewBinding
import com.marvel.developer.shareutils.constants.App.FAVORITE_RESULT_KEY_VALUE
import com.marvel.developer.shareutils.constants.App.HOME_REQUEST_KEY_VALUE
import com.marvel.developer.shareutils.databinding.StateErrorLoadDataBinding
import com.marvel.developer.shareutils.widgets.manyfacedview.view.FacedViewState
import com.marvel.developer.shareutils.widgets.manyfacedview.view.FacedViewState.Companion.CONTENT
import com.marvel.developer.shareutils.widgets.manyfacedview.view.FacedViewState.Companion.EMPTY
import com.marvel.developer.shareutils.widgets.manyfacedview.view.FacedViewState.Companion.LOADING
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
@SuppressLint("NotifyDataSetChanged")
class FavoritesFragment : BaseFragmentViewBinding<FragmentFavoritesBinding>(
    FragmentFavoritesBinding::inflate
) {

    private val viewModel: FavoritesViewModel by viewModels()

    private lateinit var bindingContent: StateFavoriteContentBinding

    private lateinit var favoriteAdapterAdapter: FavoriteAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycle.addObserver(disposer)

        val viewContent = binding.stateView.getView(CONTENT) as View
        bindingContent = StateFavoriteContentBinding.bind(viewContent)

        setupToolbar()
        setupViews()

        if (viewModel.characters.isNotEmpty()) {
            favoriteAdapterAdapter.notifyDataSetChanged()
        } else {
            fetchFavoriteCharactersCached()
        }
    }

    private fun setupToolbar() {
        (mActivity as AppCompatActivity).apply {
            setSupportActionBar(binding.includeCustomToolbar.customToolbar)
            supportActionBar?.title = ""
            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            binding.includeCustomToolbar.textToolbarTitle.text = getString(R.string.favorites)
        }
    }

    private fun setupViews() {
        favoriteAdapterAdapter = FavoriteAdapter(
            presentation = viewModel.characters,
            clickAction = ::goToDetails,
            favoriteClickAction = ::deleteFavoriteFavoriteCharacterCached
        )
        bindingContent.charactersList.apply {
            setHasFixedSize(true)
            adapter = favoriteAdapterAdapter
        }
    }

    private fun fetchFavoriteCharactersCached() {
        viewModel.run {
            val toDispose = fetchFavoriteCharactersCached()
                .subscribe(
                    { changeState(it) },
                    { handleError(it) },
                    { setStateView() }
                )

            disposer.collect(toDispose)
        }
    }

    private fun changeState(event: ViewState<List<Character>>) {
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
        presentation: List<Character>
    ) {
        Timber.d("$presentation")

        viewModel.characters.apply {
            clear()
            addAll(presentation)

            favoriteAdapterAdapter.notifyDataSetChanged()
        }
    }

    private fun handleError(reason: Throwable) {
        Timber.e("$reason")

        val viewError = binding.stateView.getView(FacedViewState.ERROR) as View
        val bindingError = StateErrorLoadDataBinding.bind(viewError)
        bindingError.buttonTryAgain.apply {
            setOnClickListener {
                fetchFavoriteCharactersCached()
            }
        }
    }

    private fun setStateView() {
        binding.stateView.setState(if (viewModel.characters.isEmpty()) EMPTY else CONTENT)
    }

    private fun deleteFavoriteFavoriteCharacterCached(position: Int, character: Character) {
        viewModel.deleteFavoriteCharacterCached(character)
        favoriteAdapterAdapter.notifyItemRemoved(position)

        setStateView()

        setFragmentResult(
            HOME_REQUEST_KEY_VALUE,
            bundleOf(FAVORITE_RESULT_KEY_VALUE to viewModel.deletedFavorites)
        )
    }

    private fun goToDetails(character: Character) {
        val action = FavoritesFragmentDirections.actionFavoritesFragmentToDetailsFragment(character)
        findNavController().navigate(action)
    }
}