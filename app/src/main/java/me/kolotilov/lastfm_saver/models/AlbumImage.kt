package me.kolotilov.lastfm_saver.models

/**
 * Image data.
 *
 * @param uri Uri.
 * @param filePath File.
 */
data class AlbumImage(
    val uri: String,
    val filePath: String?
)

fun String.toImageLocal(): AlbumImage {
    return AlbumImage(
        uri = this,
        filePath = null
    )
}