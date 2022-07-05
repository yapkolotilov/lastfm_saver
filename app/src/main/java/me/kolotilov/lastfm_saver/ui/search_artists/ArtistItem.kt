package me.kolotilov.lastfm_saver.ui.search_artists

import android.net.Uri
import androidx.core.net.toUri
import me.kolotilov.lastfm_saver.models.Artist
import me.kolotilov.lastfm_saver.presentation.common.ListItem

data class ArtistItem(
    val name: String,
    val listeners: Int,
    val imageUri: Uri
) : ListItem

fun Artist.toArtistItem(): ArtistItem {
    return ArtistItem(
        name = name,
        listeners = listeners,
        imageUri = imageUri.toUri()
    )
}