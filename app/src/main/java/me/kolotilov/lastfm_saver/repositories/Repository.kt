package me.kolotilov.lastfm_saver.repositories

import kotlinx.coroutines.flow.Flow
import me.kolotilov.lastfm_saver.models.AlbumDetails
import me.kolotilov.lastfm_saver.models.AlbumId
import me.kolotilov.lastfm_saver.models.ArtistAlbums
import me.kolotilov.lastfm_saver.models.SearchArtistResult
import me.kolotilov.lastfm_saver.repositories.network.LastFmRepository
import me.kolotilov.lastfm_saver.repositories.persistance.repositories.LocalRepository

interface Repository {

    /**
     * Search artists by name.
     *
     * @param name Artist name.
     */
    suspend fun searchArtists(name: String, page: Int, pageSize: Int): SearchArtistResult

    /**
     * Get artist albums.
     *
     * @param artist Artist name.
     */
    suspend fun getArtistAlbums(artist: String, page: Int, pageSize: Int): ArtistAlbums

    /**
     * Get artist details.
     *
     * @param id Album id.
     */
    suspend fun getAlbumDetails(id: AlbumId): AlbumDetails

    fun getSavedAlbum(id: AlbumId): Flow<AlbumDetails?>

    /**
     * Returns saved albums.
     */
    fun getSavedAlbums(): Flow<List<AlbumDetails>>

    /**
     * Save album.
     *
     * @param album Album.
     */
    suspend fun save(album: AlbumDetails)

    /**
     * Soft delete album.
     *
     * @param id Album id.
     */
    suspend fun softDelete(id: AlbumId)

    /**
     * Reverts soft deletion.
     *
     * @param id Album id.
     */
    suspend fun revertSoftDelete(id: AlbumId)

    /**
     * Hard delete albums.
     *
     */
    suspend fun hardDeleteAlbums()
}

class RepositoryImpl(
    private val lastFmRepository: LastFmRepository,
    private val localRepository: LocalRepository
) : Repository {

    override suspend fun searchArtists(name: String, page: Int, pageSize: Int): SearchArtistResult {
        return lastFmRepository.searchArtists(name, page, pageSize)
    }

    override suspend fun getArtistAlbums(artist: String, page: Int, pageSize: Int): ArtistAlbums {
        return lastFmRepository.getArtistAlbums(artist, page, pageSize)
    }

    override fun getSavedAlbum(id: AlbumId): Flow<AlbumDetails?> {
        return localRepository.getAlbum(id)
    }

    override suspend fun getAlbumDetails(id: AlbumId): AlbumDetails {
        return lastFmRepository.getAlbumDetails(id)
    }

    override fun getSavedAlbums(): Flow<List<AlbumDetails>> {
        return localRepository.getAlbums()
    }

    override suspend fun save(album: AlbumDetails) {
        val a = lastFmRepository.getAlbumDetails(album.id())
        val image = a.image?.uri?.let { lastFmRepository.downloadImage(it) }
        return localRepository.saveAlbum(album, image)
    }

    override suspend fun softDelete(id: AlbumId) {
        localRepository.softDeleteAlbum(id)
    }

    override suspend fun revertSoftDelete(id: AlbumId) {
        localRepository.revertSoftDeleteAlbum(id)
    }

    override suspend fun hardDeleteAlbums() {
        localRepository.hardDeleteAlbums()
    }
}