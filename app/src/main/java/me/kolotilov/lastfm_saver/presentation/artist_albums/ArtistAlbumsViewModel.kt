package me.kolotilov.lastfm_saver.presentation.artist_albums

import androidx.lifecycle.viewModelScope
import androidx.paging.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.kolotilov.lastfm_saver.models.AlbumId
import me.kolotilov.lastfm_saver.presentation.common.BaseViewModel
import me.kolotilov.lastfm_saver.presentation.common.ListItem
import me.kolotilov.lastfm_saver.presentation.common.ResourceProvider
import me.kolotilov.lastfm_saver.repositories.Repository
import me.kolotilov.lastfm_saver.repositories.common.PAGE_SIZE
import me.kolotilov.lastfm_saver.repositories.network.paging.ArtistAlbumsPagingSource
import me.kolotilov.lastfm_saver.ui.artist_albums.AlbumPreviewItem
import me.kolotilov.lastfm_saver.ui.artist_albums.toItem

/**
 * Artist Albums ViewModel.
 */
class ArtistAlbumsViewModel(
    private val dataProvider: ArtistAlbumsPagingSource.Provider,
    private val resourceProvider: ResourceProvider,
    private val repository: Repository
) : BaseViewModel() {

    val data: Flow<ArtistDetailsData> get() = _dataFlow
    private val _dataFlow = MutableSharedFlow<ArtistDetailsData>(1)

    /**
     * Artist data.
     */
    val albums: Flow<PagingData<ListItem>> = Pager(
        PagingConfig(pageSize = PAGE_SIZE)
    ) {
        dataProvider.create()
    }.flow
        .map { albums ->
            albums.map { it.toItem(resourceProvider, _savedAlbumsIds.contains(it.id())) as ListItem }
        }
        .cachedIn(viewModelScope)

    /**
     * Show album.
     */
    val showAlbum: Flow<AlbumId> get() = _showAlbumFlow
    private val _showAlbumFlow = MutableSharedFlow<AlbumId>(0)

    private var _savedAlbumsIds: MutableSet<AlbumId> = mutableSetOf()

    /**
     * Init viewModel.
     *
     * @param artist Artist name.
     */
    fun init(artist: String) {
        viewModelScope.launch {
            _savedAlbumsIds = repository.getSavedAlbums().map { it.id() }.toMutableSet()
            dataProvider.invalidate(artist)
            val result = runHandling { repository.getArtistAlbums(artist, 1, 0) }.getOrNull() ?: return@launch
            _dataFlow.emit(
                ArtistDetailsData(
                    artistName = result.artistName
                )
            )
        }
    }

    /**
     * On album clicked.
     *
     * @param album Album.
     */
    fun onAlbumClick(album: AlbumPreviewItem) {
        viewModelScope.launch {
            _showAlbumFlow.emit(album.id())
        }
    }

    /**
     * On album save clicked.
     *
     * @param album Album.
     */
    fun onSaveClicked(album: AlbumPreviewItem) {
        viewModelScope.launch {
            if (album.saved) {
                val albumDetails = runHandling { repository.getAlbumDetails(album.id()) }.getOrNull()
                    ?: return@launch
                repository.save(albumDetails)
                _savedAlbumsIds.add(album.id())
            } else {
                repository.delete(album.id())
                _savedAlbumsIds.remove(album.id())
            }
        }
    }
}
