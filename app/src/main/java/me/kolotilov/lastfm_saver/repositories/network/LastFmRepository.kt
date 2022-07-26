package me.kolotilov.lastfm_saver.repositories.network

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.kolotilov.lastfm_saver.models.AlbumDetails
import me.kolotilov.lastfm_saver.models.AlbumId
import me.kolotilov.lastfm_saver.models.ArtistAlbums
import me.kolotilov.lastfm_saver.models.SearchArtistResult
import me.kolotilov.lastfm_saver.repositories.network.dtos.toAlbumDetails
import me.kolotilov.lastfm_saver.repositories.network.dtos.toArtistAlbums
import me.kolotilov.lastfm_saver.repositories.network.dtos.toSearchArtistResult

/**
 * Last Fm Repository.
 */
interface LastFmRepository {

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

    suspend fun downloadImage(uri: String): Bitmap
}

class LastFmRepositoryImpl(
    private val api: LastFmApi,
    private val context: Context
) : LastFmRepository {

    override suspend fun searchArtists(name: String, page: Int, pageSize: Int): SearchArtistResult {
        return withContext(Dispatchers.IO) {
            api.searchArtists(name, page, pageSize).toSearchArtistResult()
        }
    }

    override suspend fun getArtistAlbums(artist: String, page: Int, pageSize: Int): ArtistAlbums {
        return withContext(Dispatchers.IO) {
            Log.e("BRUH", "artist = $artist")
            api.getArtistAlbums(artist, page, pageSize).toArtistAlbums()
        }
    }

    override suspend fun getAlbumDetails(id: AlbumId): AlbumDetails {
        return withContext(Dispatchers.IO) {
            api.getAlbumInfo(id.artist, id.album).toAlbumDetails()
        }
    }

    override suspend fun downloadImage(uri: String): Bitmap {
        return withContext(Dispatchers.IO) {
            Glide.with(context)
                .asBitmap()
                .load(uri.toUri())
                .submit(SIZE_ORIGINAL, SIZE_ORIGINAL)
                .get()
        }
    }
}