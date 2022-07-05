package me.kolotilov.lastfm_saver.models

/**
 * Found artist.
 *
 * @param name Name.
 * @param listeners Listeners count.
 * @param imageUri Image URL.
 */
data class Artist(
    val name: String,
    val listeners: Int,
    val imageUri: String,
    val uri: String
)