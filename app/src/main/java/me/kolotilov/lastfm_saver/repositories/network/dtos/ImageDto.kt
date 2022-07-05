package me.kolotilov.lastfm_saver.repositories.network.dtos

import com.google.gson.annotations.SerializedName
import me.kolotilov.lastfm_saver.models.AlbumImage

data class ImageDto(
    @SerializedName("#text")
    val uri: String? = null,
    @SerializedName("size")
    val size: String? = null,
)


fun ImageDto.toImage(): AlbumImage? {
    return uri?.let { AlbumImage(uri, filePath = null) }
}