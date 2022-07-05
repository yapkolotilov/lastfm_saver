package me.kolotilov.lastfm_saver.common

import me.kolotilov.lastfm_saver.models.*
import me.kolotilov.lastfm_saver.repositories.Repository

class MockRepositoryImpl : Repository {

    // Mock localRepository so we don't need to reset state.
    private var _savedAlbums = mutableMapOf<AlbumId, AlbumDetails>()

    override suspend fun searchArtists(name: String, page: Int, pageSize: Int): SearchArtistResult {
        return SearchArtistResult(
            query = name,
            totalSize = 5,
            artists = Mock.artists
        )
    }

    override suspend fun getArtistAlbums(artist: String, page: Int, pageSize: Int): ArtistAlbums {
        return ArtistAlbums(
            artistName = "Cher",
            albums = Mock.albums.map { it.toAlbumPreview() },
            totalSize = 20,
        )
    }

    override suspend fun getAlbumDetails(id: AlbumId): AlbumDetails {
        return Mock.albums.first { it.id() == id }
    }

    override suspend fun getSavedAlbum(id: AlbumId): AlbumDetails? {
        return _savedAlbums[id]
    }

    override suspend fun getSavedAlbums(): List<AlbumDetails> {
        return _savedAlbums.values.toList()
    }

    override suspend fun save(album: AlbumDetails) {
        _savedAlbums[album.id()] = album
    }

    override suspend fun delete(id: AlbumId) {
        _savedAlbums.remove(id)
    }

    fun clear() {
        _savedAlbums.clear()
    }
}