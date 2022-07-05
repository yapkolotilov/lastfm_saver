package me.kolotilov.lastfm_saver.ui.search_artists

import androidx.paging.LoadState
import me.kolotilov.lastfm_saver.databinding.SearchArtistItemLoaderBinding
import me.kolotilov.lastfm_saver.ui.common.AdapterDelegate
import me.kolotilov.lastfm_saver.ui.common.adapterDelegate
import me.kolotilov.lastfm_saver.ui.common.recycler.LoadStateDelegateAdapter

fun searchArtistsLoaderAdapter(): LoadStateDelegateAdapter {
    return LoadStateDelegateAdapter(
        loaderItem()
    )
}

private fun loaderItem(): AdapterDelegate<LoadState> {
    return adapterDelegate<LoadState, LoadState, SearchArtistItemLoaderBinding>(
        inflate = SearchArtistItemLoaderBinding::inflate
    )
}