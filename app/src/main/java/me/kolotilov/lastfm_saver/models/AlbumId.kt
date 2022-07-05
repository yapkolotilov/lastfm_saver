package me.kolotilov.lastfm_saver.models

/**
 * Album ID (as mbid is not always set).
 *
 * @param artist Artist.
 * @param album Album.
 */
data class AlbumId(
    val artist: String,
    val album: String
)
