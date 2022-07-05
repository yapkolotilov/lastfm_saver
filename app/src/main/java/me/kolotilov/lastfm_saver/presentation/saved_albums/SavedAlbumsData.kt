package me.kolotilov.lastfm_saver.presentation.saved_albums

import me.kolotilov.lastfm_saver.presentation.common.ListItem

data class SavedAlbumsData(
    val items: List<ListItem>,
    val recyclerVisibility: Boolean,
    val placeholderVisibility: Boolean
)
