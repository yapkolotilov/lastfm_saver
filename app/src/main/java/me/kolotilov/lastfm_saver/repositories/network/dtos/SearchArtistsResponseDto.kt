package me.kolotilov.lastfm_saver.repositories.network.dtos

import com.google.gson.annotations.SerializedName
import me.kolotilov.lastfm_saver.models.Artist
import me.kolotilov.lastfm_saver.models.SearchArtistResult
import me.kolotilov.lastfm_saver.models.orZero

data class SearchArtistsResponseDto(
    @SerializedName("results")
    val result: ResultDto? = null,
) {
    data class ResultDto(
        @SerializedName("@attr")
        val query: QueryDto? = null,
        @SerializedName("opensearch:totalResults")
        val totalResults: Int?,
        @SerializedName("artistmatches")
        val artistmatches: ArtistMatchesDto? = null
    ) {

        data class ArtistMatchesDto(
            @SerializedName("artist")
            val artists: List<ArtistDto>
        ) {
            data class ArtistDto(
                @SerializedName("name")
                val name: String? = null,
                @SerializedName("listeners")
                val listeners: Int? = null,
                @SerializedName("url")
                val uri: String? = null,
                @SerializedName("image")
                val images: List<ImageDto>
            )
        }

        data class QueryDto(
            @SerializedName("for")
            val query: String? = null
        )
    }
}


fun SearchArtistsResponseDto.toSearchArtistResult(): SearchArtistResult {
    return SearchArtistResult(
        query = result?.query?.query.orEmpty(),
        totalSize = result?.totalResults.orZero(),
        artists = result?.artistmatches?.artists?.map { it.toArtist() }.orEmpty()
    )
}

private fun SearchArtistsResponseDto.ResultDto.ArtistMatchesDto.ArtistDto.toArtist(): Artist {
    return Artist(
        name = name.orEmpty(),
        listeners = listeners.orZero(),
        imageUri = images.lastOrNull()?.uri.orEmpty(),
        uri = uri.orEmpty()
    )
}
