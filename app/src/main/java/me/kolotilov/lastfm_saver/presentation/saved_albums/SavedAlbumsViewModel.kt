package me.kolotilov.lastfm_saver.presentation.saved_albums

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import me.kolotilov.lastfm_saver.models.AlbumId
import me.kolotilov.lastfm_saver.presentation.common.BaseViewModel
import me.kolotilov.lastfm_saver.presentation.common.ResourceProvider
import me.kolotilov.lastfm_saver.repositories.Repository
import me.kolotilov.lastfm_saver.ui.artist_albums.AlbumPreviewItem
import me.kolotilov.lastfm_saver.ui.artist_albums.toItem

/**
 * Saved Albums ViewModel.
 */
class SavedAlbumsViewModel(
    private val repository: Repository,
    private val resourceProvider: ResourceProvider
) : BaseViewModel() {

    /**
     * Saved albums.
     */
    val savedAlbums: Flow<SavedAlbumsData> get() = _savedAlbumsFlow

    /**
     * Show album.
     */
    val showAlbum: Flow<AlbumId> get() = _showAlbumFlow

    /**
     * Show search artists.
     */
    val searchArtists: Flow<Unit> get() = _searchArtistsFlow

    private val _savedAlbumsFlow = MutableSharedFlow<SavedAlbumsData>(1)
    private val _showAlbumFlow = MutableSharedFlow<AlbumId>()
    private val _searchArtistsFlow = MutableSharedFlow<Unit>()

    override fun onAttached() {
        super.onAttached()
        viewModelScope.launch {
            loadData()
        }
    }

    /**
     * On search icon clicked.
     */
    fun onAlbumClicked(album: AlbumPreviewItem) {
        viewModelScope.launch {
            _showAlbumFlow.emit(album.id())
        }
    }

    /**
     * On save button clicked.
     */
    fun onSaveClicked(album: AlbumPreviewItem) {
        viewModelScope.launch {
            repository.delete(album.id())
            loadData()
        }
    }

    /**
     * On search button clicked.
     */
    fun onSearchClicked() {
        viewModelScope.launch {
            _searchArtistsFlow.emit(Unit)
        }
    }

    private suspend fun loadData() {
        val items = repository.getSavedAlbums()
        val data = SavedAlbumsData(
            items = items.map { it.toItem(resourceProvider, true) },
            recyclerVisibility = items.isNotEmpty(),
            placeholderVisibility = items.isEmpty()
        )
        _savedAlbumsFlow.emit(data)
    }
}