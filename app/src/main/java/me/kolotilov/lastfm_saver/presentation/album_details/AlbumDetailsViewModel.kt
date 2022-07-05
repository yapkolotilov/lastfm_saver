package me.kolotilov.lastfm_saver.presentation.album_details

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
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

    val saveButtonText: Flow<String> get() = _saveButtonTextFlow
    private val _saveButtonTextFlow = MutableSharedFlow<String>(1)

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

            _dataFlow.emit(_albumCache.toAlbumDetailsData(resourceProvider))
            _tracksFlow.emit(_albumCache.tracks.map { it.toTrackItem() })
        }
    }

    fun onSaveClicked() {
        if (!::_albumCache.isInitialized)
            return
        viewModelScope.launch {
            val saved = repository.getSavedAlbums().any { it.id() == _albumCache.id() }
            if (saved) {
                repository.delete(_albumCache.id())
                _saveButtonTextFlow.emit(resourceProvider.getString(R.string.button_save))
            } else {
                runHandling { repository.save(_albumCache) }
                _saveButtonTextFlow.emit(resourceProvider.getString(R.string.button_delete))
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            loadAlbum(_albumId)
        }
    }

    private suspend fun loadAlbum(id: AlbumId) {
        _dataFlow.emit(AlbumDetailsData.Loading)
        _tracksFlow.emit(List(5) { LoaderItem() })

        val localAlbum = repository.getSavedAlbum(id)
        if (localAlbum != null) {
            _albumCache = localAlbum
            _saveButtonTextFlow.emit(resourceProvider.getString(R.string.button_delete))
            return
        }

        val remoteAlbum = runHandling { repository.getAlbumDetails(id) }.getOrElse {
            _dataFlow.emit(AlbumDetailsData.Error)
            return
        }
        _albumCache = remoteAlbum

        _saveButtonTextFlow.emit(resourceProvider.getString(R.string.button_save))
    }
}