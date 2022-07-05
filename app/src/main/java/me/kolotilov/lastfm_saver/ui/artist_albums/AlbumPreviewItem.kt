package me.kolotilov.lastfm_saver.ui.artist_albums

import me.kolotilov.lastfm_saver.R
import me.kolotilov.lastfm_saver.models.AlbumDetails
import me.kolotilov.lastfm_saver.models.AlbumId
import me.kolotilov.lastfm_saver.models.AlbumPreview
import me.kolotilov.lastfm_saver.models.AlbumImage
import me.kolotilov.lastfm_saver.presentation.common.ListItem
import me.kolotilov.lastfm_saver.presentation.common.ResourceProvider

/**
 * Album preview.
 *
 * @param name Name.
 * @param playCount Play count.
 * @param artist Artist info.
 * @param imageUri Image URI.
 */
data class AlbumPreviewItem(
    val name: String,
    val playCount: String,
    val image: AlbumImage?,
    val artist: String,
    var saved: Boolean
) : ListItem {

    fun id(): AlbumId {
        return AlbumId(
            artist = artist,
            album = name
        )
    }
}

fun AlbumPreview.toItem(resourceProvider: ResourceProvider, saved: Boolean): AlbumPreviewItem {
    return AlbumPreviewItem(
        name = name,
        image = image,
        playCount = resourceProvider.getString(R.string.text_artist_albums_playcount, playCount),
        artist = artist,
        saved = saved
    )
}

fun AlbumDetails.toItem(resourceProvider: ResourceProvider, saved: Boolean): AlbumPreviewItem {
    return AlbumPreviewItem(
        name = name,
        image = image,
        playCount = resourceProvider.getString(R.string.text_artist_albums_playcount, playCount),
        artist = artist,
        saved = saved
    )
}