package me.kolotilov.lastfm_saver.models

/**
 * Artist albums.
 *
 * @param artistName Artist's name.
 * @param albums Top albums.
 */
data class ArtistAlbums(
    val artistName: String,
    val albums: List<AlbumPreview>,
    val totalSize: Int
)