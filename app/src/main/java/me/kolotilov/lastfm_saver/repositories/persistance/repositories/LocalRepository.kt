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
     * Soft delete album.
     *
     * @param id Album id.
     */
    suspend fun softDeleteAlbum(id: AlbumId)

    /**
     * Reverts soft deletion of an album.
     *
     * @param id Album id.
     */
    suspend fun revertSoftDeleteAlbum(id: AlbumId)

    /**
     * Delete albums permanently.
     *
     */
    suspend fun hardDeleteAlbums()
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

    override suspend fun softDeleteAlbum(id: AlbumId) {
        withContext(Dispatchers.IO) {
            dao.softDelete(id.artist, id.album)
            logger.debug("Soft deleted ${id.album}")
        }
    }

    override suspend fun revertSoftDeleteAlbum(id: AlbumId) {
        withContext(Dispatchers.IO) {
            dao.revertSoftDelete(id.artist, id.album)
            logger.debug("Reverted soft deletion ${id.album}")
        }
    }

    override suspend fun hardDeleteAlbums() {
        val savedAlbumIds = dao.getAlbumsToDelete().map { AlbumId(it.artist, it.name) }
        for (id in savedAlbumIds)
            hardDeleteAlbum(id)
    }

    private suspend fun hardDeleteAlbum(id: AlbumId) {
        withContext(Dispatchers.IO) {
            dao.delete(id.artist, id.album)
            val imageFile = fileHelper.imageFile(id)
            runCatching {
                imageFile.delete()
            }
            logger.debug("Hard deleted ${id.album}")
        }
    }
}