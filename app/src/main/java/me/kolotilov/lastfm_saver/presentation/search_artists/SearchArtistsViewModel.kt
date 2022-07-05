package me.kolotilov.lastfm_saver.presentation.search_artists

import androidx.lifecycle.viewModelScope
import androidx.paging.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.kolotilov.lastfm_saver.R
import me.kolotilov.lastfm_saver.presentation.common.BaseViewModel
import me.kolotilov.lastfm_saver.presentation.common.ListItem
import me.kolotilov.lastfm_saver.presentation.common.ResourceProvider
import me.kolotilov.lastfm_saver.repositories.Repository
import me.kolotilov.lastfm_saver.repositories.common.PAGE_SIZE
import me.kolotilov.lastfm_saver.repositories.network.paging.SearchArtistsPagingSource
import me.kolotilov.lastfm_saver.ui.search_artists.ArtistItem
import me.kolotilov.lastfm_saver.ui.search_artists.toArtistItem
import org.koin.core.component.get

/**
 * Search Artists ViewModel.
 */
class SearchArtistsViewModel(
    private val resourceProvider: ResourceProvider,
    private val dataProvider: SearchArtistsPagingSource.Provider
) : BaseViewModel() {

    /**
     * Artists found.
     */
    val artists: Flow<PagingData<ListItem>> = Pager(
        PagingConfig(pageSize = PAGE_SIZE)
    ) {
        dataProvider.create()
    }.flow
        .map { list -> list.map { it.toArtistItem() as ListItem } }
        .cachedIn(viewModelScope)

    /**
     * Show artist's details.
     */
    val showArtistAlbums: Flow<String> get() = _showArtistAlbumsFlow
    private val _showArtistAlbumsFlow = MutableSharedFlow<String>(0)

    val query: Flow<String> get() = _queryFlow
    private val _queryFlow = MutableSharedFlow<String>(1)

    /**
     * Perform search request.
     */
    fun search(query: String) {
        if (query.isEmpty()) {
            showMessage(resourceProvider.getString(R.string.message_enter_query))
            return
        }
        dataProvider.invalidate(query)
    }

    /**
     * On artist clicked.
     */
    fun onArtistClick(artist: ArtistItem) {
        viewModelScope.launch {
            _showArtistAlbumsFlow.emit(artist.name)
        }
    }

    override fun onAttached() {
        super.onAttached()
        showMessage(get<Repository>()::class.simpleName)
        viewModelScope.launch {
            _queryFlow.emit(dataProvider.query)
        }
    }
}