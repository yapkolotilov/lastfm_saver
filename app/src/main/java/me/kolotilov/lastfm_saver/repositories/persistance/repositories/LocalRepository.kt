package me.kolotilov.lastfm_saver.repositories.persistance.repositories

import android.graphics.Bitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import me.kolotilov.lastfm_saver.models.AlbumDetails
import me.kolotilov.lastfm_saver.models.AlbumId
import me.kolotilov.lastfm_saver.repositories.common.FileHelper
import me.kolotilov.lastfm_saver.repositories.persistance.entities.toAlbumDetails
import me.kolotilov.lastfm_saver.repositories.persistance.entities.toAlbumToTagsAndTracks
import me.kolotilov.lastfm_saver.utils.Logger

/**
 * Local repository.
 */
interface LocalRepository {

    /**
     * Returns album by id.
     */
    fun getAlbum(id: AlbumId): Flow<AlbumDetails?>

    /**
     * Get all albums.
     */
    fun getAlbums(): Flow<List<AlbumDetails>>

    /**
     * Save album.
     *
     * @param album Album.
     */
    suspend fun saveAlbum(album: AlbumDetails, image: Bitmap?)

    /**
     * Delete album.
     *
     * @param id Album id.
     */
    suspend fun deleteAlbum(id: AlbumId)
}

class LocalRepositoryImpl(
    private val dao: AlbumsDao,
    private val fileHelper: FileHelper
) : LocalRepository {

    private val logger = Logger(this)

    override fun getAlbum(id: AlbumId): Flow<AlbumDetails?> {
        return dao.getAlbum(id)
            .map { it?.toAlbumDetails() }
            .flowOn(Dispatchers.IO)
    }

    override fun getAlbums(): Flow<List<AlbumDetails>> {
        return dao.getAll()
            .map { it.map { it.toAlbumDetails() } }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun saveAlbum(album: AlbumDetails, image: Bitmap?) {
        withContext(Dispatchers.IO) {
            dao.saveAlbum(album.toAlbumToTagsAndTracks())
            if (image != null) {
                val imageFile = fileHelper.imageFile(album.id())
                runCatching {
                    image.compress(Bitmap.CompressFormat.JPEG, 100, imageFile.outputStream())
                }
            }
            logger.debug("Saved $album")
        }
    }

    override suspend fun deleteAlbum(id: AlbumId) {
        withContext(Dispatchers.IO) {
            dao.delete(id.artist, id.album)
            val imageFile = fileHelper.imageFile(id)
            runCatching {
                imageFile.delete()
            }
            logger.debug("Deleted ${id.album}")
        }
    }
}