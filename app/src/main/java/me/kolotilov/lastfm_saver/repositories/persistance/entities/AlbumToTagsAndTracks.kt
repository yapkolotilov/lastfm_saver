package me.kolotilov.lastfm_saver.repositories.persistance.entities

import me.kolotilov.lastfm_saver.models.AlbumDetails
import me.kolotilov.lastfm_saver.models.AlbumImage

data class AlbumToTagsAndTracks(
    val album: AlbumEntity,
    val tags: List<AlbumEntity.TagEntity>,
    val tracks: List<AlbumEntity.TrackEntity>
)

fun AlbumToTagsAndTracks.toAlbumDetails(): AlbumDetails {
    val image = if (album.imageUri != null)
        AlbumImage(album.imageUri, album.imagePath)
    else null
    return AlbumDetails(
        artist = album.artist,
        image = image,
        name = album.name,
        playCount = album.playCount,
        uri = album.uri,
        tags = tags.map { it.toTag() },
        tracks = tracks.map { it.toTrack() },
    )
}

fun AlbumDetails.toAlbumToTagsAndTracks(): AlbumToTagsAndTracks {
    return AlbumToTagsAndTracks(
        album = toAlbumEntity(),
        tags = tags.map { it.toTagEntity(id()) },
        tracks = tracks.map { it.toTrackEntity(id()) }
    )
}