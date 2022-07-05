package me.kolotilov.lastfm_saver.ui.album_details

import me.kolotilov.lastfm_saver.databinding.ItemTrackBinding
import me.kolotilov.lastfm_saver.databinding.ItemTrackLoaderBinding
import me.kolotilov.lastfm_saver.presentation.common.ListItem
import me.kolotilov.lastfm_saver.ui.common.AdapterDelegate
import me.kolotilov.lastfm_saver.ui.common.adapterDelegate
import me.kolotilov.lastfm_saver.ui.common.recycler.DelegateAdapter


fun albumDetailsAdapter(): DelegateAdapter<ListItem> {
    return DelegateAdapter(
        trackDelegate(),
        loaderDelegate()
    )
}

private fun trackDelegate(): AdapterDelegate<ListItem> {
    return adapterDelegate<ListItem, TrackItem, ItemTrackBinding>(
        inflate = ItemTrackBinding::inflate,
        bind = { item ->
            trackNameText.text = item.name
            durationText.text = item.duration
        }
    )
}

private fun loaderDelegate(): AdapterDelegate<ListItem> {
    return adapterDelegate<ListItem, LoaderItem, ItemTrackLoaderBinding>(
        inflate = ItemTrackLoaderBinding::inflate
    )
}