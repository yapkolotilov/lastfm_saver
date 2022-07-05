package me.kolotilov.lastfm_saver.ui.artist_albums

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import me.kolotilov.lastfm_saver.R
import me.kolotilov.lastfm_saver.databinding.FragmentArtistAlbumsBinding
import me.kolotilov.lastfm_saver.presentation.artist_albums.ArtistAlbumsViewModel
import me.kolotilov.lastfm_saver.presentation.common.ListItem
import me.kolotilov.lastfm_saver.ui.common.BaseFragment
import me.kolotilov.lastfm_saver.ui.common.GridHelper
import me.kolotilov.lastfm_saver.ui.common.recycler.PagedDelegateAdapter
import me.kolotilov.lastfm_saver.ui.navigation.Router
import me.kolotilov.lastfm_saver.ui.utils.invoke
import me.kolotilov.lastfm_saver.ui.utils.switchViews
import org.koin.androidx.viewmodel.ext.android.viewModel


class ArtistAlbumsFragment : BaseFragment<FragmentArtistAlbumsBinding>() {

    override val toolbarId: Int = R.id.toolbar
    override val viewModel: ArtistAlbumsViewModel by viewModel()
    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentArtistAlbumsBinding
        get() = FragmentArtistAlbumsBinding::inflate

    private lateinit var adapter: PagedDelegateAdapter<ListItem>

    override fun readArgs() {
        super.readArgs()
        val args by navArgs<ArtistAlbumsFragmentArgs>()
        viewModel.init(args.artist)
    }

    override fun initViews() {
        super.initViews()
        viewBinding {
            val spansCount = GridHelper(requireActivity()).albumGridSize()
            recycler.layoutManager = GridLayoutManager(requireContext(), spansCount)
            adapter = artistAlbumsAdapter(
                onAlbumClick = viewModel::onAlbumClick,
                onSaveClicked = viewModel::onSaveClicked
            )
            recycler.adapter = adapter
            adapter.loadStateFlow.subscribe {
                it.switchViews(
                    recycler = recycler,
                    placeholder = placeholder,
                    loader = loader,
                    error = error
                )
            }
        }
    }

    override fun subscribe() {
        super.subscribe()
        viewBinding {
            viewModel.data.subscribe { data ->
                toolbar.title = data.artistName
            }

            viewModel.albums.subscribe { albums ->
                adapter.submitData(albums)
            }

            viewModel.showAlbum.subscribe {
                Router(findNavController()).showAlbumDetails(it)
            }
        }
    }
}