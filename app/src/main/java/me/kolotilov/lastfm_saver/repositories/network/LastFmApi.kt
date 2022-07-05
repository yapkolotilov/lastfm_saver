package me.kolotilov.lastfm_saver.repositories.network

import me.kolotilov.lastfm_saver.repositories.network.dtos.AlbumInfoResponseDto
import me.kolotilov.lastfm_saver.repositories.network.dtos.ArtistAlbumsResponseDto
import me.kolotilov.lastfm_saver.repositories.network.dtos.SearchArtistsResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface LastFmApi {

    private companion object {

        const val URL = "2.0/"
    }

    @GET(URL)
    suspend fun searchArtists(
        @Query("artist") query: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("method") method: String = "artist.search"
    ): SearchArtistsResponseDto

    @GET(URL)
    suspend fun getArtistAlbums(
        @Query("artist") artist: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("method") method: String = "artist.gettopalbums"
    ): ArtistAlbumsResponseDto

    @GET(URL)
    suspend fun getAlbumInfo(
        @Query("artist") artist: String,
        @Query("album") album: String,
        @Query("method") method: String = "album.getInfo"
    ): AlbumInfoResponseDto
}