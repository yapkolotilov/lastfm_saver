package me.kolotilov.lastfm_saver.repositories.common

import android.content.Context
import me.kolotilov.lastfm_saver.models.AlbumId
import java.io.File

/**
 * File helper.
 */
interface FileHelper {

    /**
     * Returns file containing album image.
     *
     * @param albumId Album ID.
     */
    fun imageFile(albumId: AlbumId): File
}

class FileHelperImpl(
    private val context: Context
) : FileHelper {

    override fun imageFile(albumId: AlbumId): File {
        return File(context.filesDir, "${albumId.artist}_${albumId.album}.jpg")
    }
}