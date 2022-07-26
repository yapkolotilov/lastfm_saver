package me.kolotilov.lastfm_saver.repositories.network.dtos

import com.google.gson.annotations.SerializedName
import me.kolotilov.lastfm_saver.models.AlbumPreview
import me.kolotilov.lastfm_saver.models.ArtistAlbums
import me.kolotilov.lastfm_saver.models.orZero

data class ArtistAlbumsResponseDto(
    @SerializedName("topalbums")
    val topAlbums: TopAlbumsDto? = null
) {

    data class TopAlbumsDto(
        @SerializedName("@attr")
        val artist: ArtistDto? = null,
        @SerializedName("album")
        val albums: List<AlbumDto>? = null
    ) {

        data class AlbumDto(
            @SerializedName("name")
            val name: String? = null,
            @SerializedName("playcount")
            val playCount: Int? = null,
            @SerializedName("artist")
            val artist: ArtistDto? = null,
            @SerializedName("image")
            val images: List<ImageDto>? = null
        ) {

            data class ArtistDto(
                @SerializedName("name")
                val name: String? = null,
                @SerializedName("url")
                val uri: String? = null
            )
        }

        data class ArtistDto(
            @SerializedName("artist")
            val artist: String? = null,
            @SerializedName("total")
            val total: Int? = null
        )
    }
}


fun ArtistAlbumsResponseDto.toArtistAlbums(): ArtistAlbums {
    return ArtistAlbums(
        artistName = topAlbums?.artist?.artist.orEmpty(),
        albums = topAlbums?.albums?.map { it.toAlbumPreview() }.orEmpty(),
        totalSize = topAlbums?.artist?.total.orZero()
    )
}

private fun ArtistAlbumsResponseDto.TopAlbumsDto.AlbumDto.toAlbumPreview(): AlbumPreview {
    return AlbumPreview(
        playCount = playCount.orZero(),
        name = name.orEmpty(),
        image = images?.lastOrNull()?.toImage(),
        artist = artist?.name.orEmpty()
    )
}