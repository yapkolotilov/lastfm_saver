package me.kolotilov.lastfm_saver.ui.saved_albums

import android.Manifest
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import me.kolotilov.lastfm_saver.R
import me.kolotilov.lastfm_saver.databinding.FragmentSavedAlbumsBinding
import me.kolotilov.lastfm_saver.presentation.common.ListItem
import me.kolotilov.lastfm_saver.presentation.saved_albums.SavedAlbumsViewModel
import me.kolotilov.lastfm_saver.ui.common.BaseFragment
import me.kolotilov.lastfm_saver.ui.common.GridHelper
import me.kolotilov.lastfm_saver.ui.common.recycler.DelegateListAdapter
import me.kolotilov.lastfm_saver.ui.navigation.Router
import me.kolotilov.lastfm_saver.ui.utils.invoke
import org.koin.androidx.viewmodel.ext.android.viewModel

class SavedAlbumsFragment : BaseFragment<FragmentSavedAlbumsBinding>() {

    private lateinit var adapter: DelegateListAdapter<ListItem>

    override val viewModel: SavedAlbumsViewModel by viewModel()
    override val toolbarId: Int = R.id.toolbar
    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSavedAlbumsBinding
        get() = FragmentSavedAlbumsBinding::inflate

    override fun initViews() {
        super.initViews()
        requestPermissions()
        viewBinding.invoke {
            val spansCount = GridHelper(requireActivity()).albumGridSize()
            recycler.layoutManager = GridLayoutManager(requireContext(), spansCount)
            adapter = savedAlbumsAdapter(
                onAlbumClick = viewModel::onAlbumClicked,
                onSaveClicked = viewModel::onSaveClicked
            )
            recycler.adapter = adapter
        }
    }

    override fun interact() {
        super.interact()
        viewBinding {
            searchButton.setOnClickListener {
                viewModel.onSearchClicked()
            }
        }
    }

    override fun subscribe() {
        super.subscribe()
        viewBinding {
            viewModel.savedAlbums.subscribe { data ->
                adapter.submitList(data.items)
                recycler.isVisible = data.recyclerVisibility
                placeholder.isVisible = data.placeholderVisibility
            }
            viewModel.showAlbum.subscribe {
                Router(findNavController()).showAlbumDetails(it)
            }
            viewModel.searchArtists.subscribe {
                Router(findNavController()).showSearchAlbums()
            }
        }
    }

    private fun requestPermissions() {
        val launcher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (!isGranted)
                Router(findNavController()).showStoragePermissionNotGrantedDialog()
        }
        launcher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }
}