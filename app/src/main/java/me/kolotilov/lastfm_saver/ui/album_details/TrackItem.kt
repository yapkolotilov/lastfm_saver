package me.kolotilov.lastfm_saver.ui.album_details

import android.net.Uri
import me.kolotilov.lastfm_saver.presentation.common.ListItem

data class TrackItem(
    val duration: String?,
    val uri: Uri,
    val name: String
) : ListItem

class LoaderItem : ListItem
