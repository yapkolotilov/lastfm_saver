package me.kolotilov.lastfm_saver.presentation.saved_albums

import androidx.annotation.StringRes
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.kolotilov.lastfm_saver.R
import me.kolotilov.lastfm_saver.domain.CancelableAction
import me.kolotilov.lastfm_saver.domain.cancelableActionRunner
import me.kolotilov.lastfm_saver.models.AlbumId
import me.kolotilov.lastfm_saver.presentation.common.BaseViewModel
import me.kolotilov.lastfm_saver.presentation.common.ResourceProvider
import me.kolotilov.lastfm_saver.repositories.Repository
import me.kolotilov.lastfm_saver.repositories.persistance.repositories.DeleteAlbumAction
import me.kolotilov.lastfm_saver.ui.artist_albums.AlbumPreviewItem
import me.kolotilov.lastfm_saver.ui.artist_albums.toItem

const val DELETION_TIMEOUT: Long = 10_000

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
    private val _savedAlbumsFlow = MutableSharedFlow<SavedAlbumsData>(1)

    /**
     * Show album.
     */
    val showAlbum: Flow<AlbumId> get() = _showAlbumFlow
    private val _showAlbumFlow = MutableSharedFlow<AlbumId>()

    /**
     * Show search artists.
     */
    val searchArtists: Flow<Unit> get() = _searchArtistsFlow
    private val _searchArtistsFlow = MutableSharedFlow<Unit>()

    /**
     * Show delete snackbar.
     */
    val deleteSnackbar: Flow<DeleteSnackbarData> get() = _deleteSnackbarFlow
    private val _deleteSnackbarFlow = MutableSharedFlow<DeleteSnackbarData>()

    private var deleteRunner: CancelableAction.Runner = cancelableActionRunner(DELETION_TIMEOUT) {
        repository.hardDeleteAlbums()
    }

    override fun onAttached() {
        super.onAttached()
        viewModelScope.launch {
            subscribeToSavedAlbums()
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
     * On delete button clicked.
     */
    fun onDeleteClicked(album: AlbumPreviewItem) {
        viewModelScope.launch {
            _deleteSnackbarFlow.emit(
                DeleteSnackbarData(
                    text = R.string.snack_title_cancel_album_deletion,
                    timeout = DELETION_TIMEOUT.toInt()
                )
            )
            deleteRunner.submit(
                DeleteAlbumAction(
                    repository = repository,
                    albumId = album.id()
                )
            )
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

    /**
     * Cancel album deletion.
     */
    fun cancelDeletion() {
        viewModelScope.launch {
            deleteRunner.cancel()
        }
    }

    private suspend fun subscribeToSavedAlbums() {
        repository.getSavedAlbums()
            .map { items -> items.map { it.toItem(resourceProvider, true) } }
            .collect { items ->
                val data = SavedAlbumsData(
                    items = items,
                    recyclerVisibility = items.isNotEmpty(),
                    placeholderVisibility = items.isEmpty()
                )
                _savedAlbumsFlow.emit(data)
            }
    }
}

data class DeleteSnackbarData(
    @StringRes
    val text: Int,
    val timeout: Int
)