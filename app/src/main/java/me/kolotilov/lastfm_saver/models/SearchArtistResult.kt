package me.kolotilov.lastfm_saver.models

/**
 * The result of search request.
 *
 * @param query Query.
 * @param artists Matches found.
 */
data class SearchArtistResult(
    val query: String,
    val totalSize: Int,
    val artists: List<Artist>,
)