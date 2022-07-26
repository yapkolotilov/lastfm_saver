package me.kolotilov.lastfm_saver.presentation.album_details

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.kolotilov.lastfm_saver.R
import me.kolotilov.lastfm_saver.models.AlbumDetails
import me.kolotilov.lastfm_saver.models.AlbumId
import me.kolotilov.lastfm_saver.presentation.common.BaseViewModel
import me.kolotilov.lastfm_saver.presentation.common.ListItem
import me.kolotilov.lastfm_saver.presentation.common.ResourceProvider
import me.kolotilov.lastfm_saver.repositories.Repository
import me.kolotilov.lastfm_saver.ui.album_details.AlbumDetailsData
import me.kolotilov.lastfm_saver.ui.album_details.LoaderItem
import me.kolotilov.lastfm_saver.ui.album_details.toAlbumDetailsData
import me.kolotilov.lastfm_saver.ui.album_details.toTrackItem

/**
 * Album Details ViewModel.
 */
class AlbumDetailsViewModel(
    private val repository: Repository,
    private val resourceProvider: ResourceProvider,
) : BaseViewModel() {

    /**
     * Album data.
     */
    val data: Flow<AlbumDetailsData> get() = _dataFlow
    private val _dataFlow = MutableSharedFlow<AlbumDetailsData>(1)

    /**
     * Tracks.
     */
    val tracks: Flow<List<ListItem>> get() = _tracksFlow
    private val _tracksFlow = MutableSharedFlow<List<ListItem>>(1)

    val saveButtonText: Flow<String>
        get() = _savedFlow
            .map {
                if (it) resourceProvider.getString(R.string.button_delete) else resourceProvider.getString(
                    R.string.button_save
                )
            }
    private val _savedFlow = MutableStateFlow(false)

    private lateinit var _albumId: AlbumId
    private lateinit var _albumCache: AlbumDetails

    /**
     * Initialize viewModel
     *
     * @param id Id.
     */
    fun init(id: AlbumId) {
        _albumId = id
        viewModelScope.launch {
            loadAlbum(id)
        }
    }

    fun onSaveClicked() {
        if (!::_albumCache.isInitialized)
            return
        viewModelScope.launch {
            if (_savedFlow.value) {
                repository.delete(_albumCache.id())
                _savedFlow.emit(false)
            } else {
                runHandling { repository.save(_albumCache) }.getOrElse { return@launch }
                _savedFlow.emit(true)
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            loadAlbum(_albumId)
        }
    }

    private suspend fun loadAlbum(id: AlbumId) {
        suspend fun showData(
            albumDetails: AlbumDetails
        ) {
            _dataFlow.emit(albumDetails.toAlbumDetailsData(resourceProvider))
            _tracksFlow.emit(albumDetails.tracks.map { it.toTrackItem() })
        }

        _dataFlow.emit(AlbumDetailsData.Loading)
        _tracksFlow.emit(List(5) { LoaderItem() })

        val localAlbum = repository.getSavedAlbum(id)
        if (localAlbum != null) {
            _albumCache = localAlbum
            _savedFlow.value = true
            showData(localAlbum)
            return
        }

        _savedFlow.value = false
        val remoteAlbum = runHandling(showError = false) { repository.getAlbumDetails(id) }
            .onFailure {
                _dataFlow.emit(AlbumDetailsData.Error)
                _tracksFlow.emit(emptyList())
                return
            }.getOrNull() ?: return

        _albumCache = remoteAlbum
        showData(remoteAlbum)
    }
}