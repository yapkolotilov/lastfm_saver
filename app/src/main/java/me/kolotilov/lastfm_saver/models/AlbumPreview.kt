package me.kolotilov.lastfm_saver.models

/**
 * Album preview.
 *
 */
data class AlbumPreview(
    val name: String,
    val playCount: Int,
    val artist: String,
    val image: AlbumImage?
) {

    fun id(): AlbumId {
        return AlbumId(
            artist = artist,
            album = name
        )
    }
}

fun AlbumDetails.toAlbumPreview(): AlbumPreview {
    return AlbumPreview(
        name = name,
        playCount = playCount,
        artist = artist,
        image = image
    )
}