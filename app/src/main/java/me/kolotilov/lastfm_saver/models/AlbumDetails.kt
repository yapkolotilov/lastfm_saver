package me.kolotilov.lastfm_saver.models

/**
 * Album data.
 */
data class AlbumDetails(
    val name: String,
    val image: AlbumImage?,
    val artist: String,
    val playCount: Int,
    val tags: List<Tag>,
    val tracks: List<Track>,
    val uri: String
) {

    data class Track(
        val durationSecs: Int?,
        val uri: String,
        val name: String
    )

    data class Tag(
        val name: String,
        val uri: String
    )

    fun id(): AlbumId {
        return AlbumId(
            artist = artist,
            album = name
        )
    }
}