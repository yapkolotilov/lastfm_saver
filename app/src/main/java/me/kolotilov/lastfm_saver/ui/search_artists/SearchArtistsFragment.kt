package me.kolotilov.lastfm_saver.ui.search_artists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import me.kolotilov.lastfm_saver.R
import me.kolotilov.lastfm_saver.databinding.FragmentSearchArtistsBinding
import me.kolotilov.lastfm_saver.presentation.common.ListItem
import me.kolotilov.lastfm_saver.presentation.search_artists.SearchArtistsViewModel
import me.kolotilov.lastfm_saver.ui.common.BaseFragment
import me.kolotilov.lastfm_saver.ui.common.DividerItemDecoration
import me.kolotilov.lastfm_saver.ui.common.recycler.PagedDelegateAdapter
import me.kolotilov.lastfm_saver.ui.navigation.Router
import me.kolotilov.lastfm_saver.ui.utils.*
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * Search artists fragment.
 */
class SearchArtistsFragment : BaseFragment<FragmentSearchArtistsBinding>() {

    override val viewModel: SearchArtistsViewModel by viewModel()
    override val toolbarId: Int = R.id.toolbar
    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSearchArtistsBinding
        get() = FragmentSearchArtistsBinding::inflate

    private lateinit var adapter: PagedDelegateAdapter<ListItem>

    override fun initViews() {
        super.initViews()
        viewBinding {
            recycler.addItemDecoration(DividerItemDecoration(requireContext()))

            adapter = searchArtistsAdapter(
                context = requireContext(),
                onArtistClick = viewModel::onArtistClick
            )
            recycler.adapter = adapter.withLoadStateFooter(
                searchArtistsLoaderAdapter(),
            )

            adapter.loadStateFlow.subscribe { states ->
                states.switchViews(
                    recycler = recycler,
                    loader = loader,
                    placeholder = placeholder,
                    error = error
                )
            }
        }
    }

    override fun interact() {
        super.interact()
        viewBinding {
            searchButton.setOnClickListener {
                search()
            }
            searchInput.setOnEnterClickListener {
                search()
            }
        }
    }

    override fun subscribe() {
        super.subscribe()
        viewBinding {
            viewModel.artists.subscribe { items ->
                adapter.submitData(items)
            }

            viewModel.query.subscribe {
                searchInput.text = it
            }

            viewModel.showArtistAlbums.subscribe {
                Router(findNavController()).showArtistAlbums(it)
            }
        }
    }

    private fun search() {
        viewBinding {
            searchInput.clearFocusAndCloseKeyboard()
            viewModel.search(
                query = viewBinding.searchInput.text
            )
        }
    }
}