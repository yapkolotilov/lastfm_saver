package me.kolotilov.lastfm_saver.repositories.network.dtos

import com.google.gson.annotations.SerializedName
import me.kolotilov.lastfm_saver.models.AlbumDetails
import me.kolotilov.lastfm_saver.models.orZero

data class AlbumInfoResponseDto(
    @SerializedName("album")
    val album: AlbumDto? = null
) {

    data class AlbumDto(
        @SerializedName("artist")
        val artist: String? = null,
        @SerializedName("tags")
        val tags: TagsDto? = null,
        @SerializedName("playcount")
        val playCount: Int? = null,
        @SerializedName("image")
        val images: List<ImageDto>? = null,
        @SerializedName("tracks")
        val tracks: TracksDto? = null,
        @SerializedName("url")
        val uri: String? = null,
        @SerializedName("name")
        val name: String? = null,
    ) {

        data class TagsDto(
            @SerializedName("tag")
            val tags: List<TagDto>? = null,
        ) {
            data class TagDto(
                @SerializedName("url")
                val uri: String? = null,
                @SerializedName("name")
                val name: String? = null
            )
        }

        data class TracksDto(
            @SerializedName("track")
            val tracks: List<TrackDto>? = null
        ) {

            data class TrackDto(
                @SerializedName("duration")
                val durationSecs: Int? = null,
                @SerializedName("url")
                val uri: String? = null,
                @SerializedName("name")
                val name: String? = null
            )
        }
    }
}


fun AlbumInfoResponseDto.toAlbumDetails(): AlbumDetails {
    return AlbumDetails(
        artist = album?.artist.orEmpty(),
        image = album?.images?.lastOrNull()?.toImage(),
        name = album?.name.orEmpty(),
        playCount = album?.playCount.orZero(),
        tags = album?.tags?.tags?.map { it.toTag() }.orEmpty(),
        tracks = album?.tracks?.tracks?.map { it.toTrack() }.orEmpty(),
        uri = album?.uri.orEmpty(),
    )
}

private fun AlbumInfoResponseDto.AlbumDto.TracksDto.TrackDto.toTrack(): AlbumDetails.Track {
    return AlbumDetails.Track(
        durationSecs = durationSecs,
        name = name.orEmpty(),
        uri = uri.orEmpty()
    )
}

private fun AlbumInfoResponseDto.AlbumDto.TagsDto.TagDto.toTag(): AlbumDetails.Tag {
    return AlbumDetails.Tag(
        name = name.orEmpty(),
        uri = uri.orEmpty()
    )
}