package me.kolotilov.lastfm_saver.repositories

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

    suspend fun getSavedAlbum(id: AlbumId): AlbumDetails?

    /**
     * Returns saved albums.
     */
    suspend fun getSavedAlbums(): List<AlbumDetails>

    /**
     * Save album.
     *
     * @param album Album.
     */
    suspend fun save(album: AlbumDetails)

    /**
     * Delete album.
     *
     * @param id Album id.
     */
    suspend fun delete(id: AlbumId)
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

    override suspend fun getSavedAlbum(id: AlbumId): AlbumDetails? {
        return localRepository.getAlbum(id)
    }

    override suspend fun getAlbumDetails(id: AlbumId): AlbumDetails {
        return lastFmRepository.getAlbumDetails(id)
    }

    override suspend fun getSavedAlbums(): List<AlbumDetails> {
        return localRepository.getAlbums()
    }

    override suspend fun save(album: AlbumDetails) {
        val a = lastFmRepository.getAlbumDetails(album.id())
        val image = a.image?.uri?.let { lastFmRepository.downloadImage(it) }
        return localRepository.saveAlbum(album, image)
    }

    override suspend fun delete(id: AlbumId) {
        localRepository.deleteAlbum(id)
    }
}