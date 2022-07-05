package me.kolotilov.lastfm_saver.ui.album_details

import android.net.Uri
import androidx.core.net.toUri
import me.kolotilov.lastfm_saver.R
import me.kolotilov.lastfm_saver.models.AlbumDetails
import me.kolotilov.lastfm_saver.models.AlbumImage
import me.kolotilov.lastfm_saver.presentation.common.ResourceProvider

sealed class AlbumDetailsData(

) {

    object Loading : AlbumDetailsData()

    data class Success(
        val name: String,
        val image: AlbumImage?,
        val artist: String,
        val playCount: String,
        val tags: List<TagItem>,
        val uri: Uri,
    ) : AlbumDetailsData()

    object Error : AlbumDetailsData()
}

fun AlbumDetails.toAlbumDetailsData(resourceProvider: ResourceProvider): AlbumDetailsData {
    return AlbumDetailsData.Success(
        name = name,
        artist = artist,
        image = image,
        playCount = resourceProvider.getString(R.string.text_artist_albums_playcount, playCount),
        uri = uri.toUri(),
        tags = tags.map { it.toTagItem() },
    )
}

fun AlbumDetails.Tag.toTagItem(): TagItem {
    return TagItem(
        name = name,
        uri = uri.toUri()
    )
}

fun AlbumDetails.Track.toTrackItem(): TrackItem {
    return TrackItem(
        duration = durationSecs?.let { "${durationSecs / 60}:${durationSecs % 60}" },
        name = name,
        uri = uri.toUri()
    )
}