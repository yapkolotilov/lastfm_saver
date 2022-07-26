package me.kolotilov.lastfm_saver.repositories.persistance.repositories

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.kolotilov.lastfm_saver.models.AlbumId
import me.kolotilov.lastfm_saver.repositories.persistance.entities.AlbumEntity
import me.kolotilov.lastfm_saver.repositories.persistance.entities.AlbumToTagsAndTracks

@Dao
abstract class AlbumsDao {

    open fun getAll(): Flow<List<AlbumToTagsAndTracks>> {
        return getAllAlbums().map { albums ->
            albums.map {
                getAlbumToTagsAndTracks(it)
            }
        }
    }

    @Transaction
    open suspend fun saveAlbum(
        album: AlbumToTagsAndTracks,
    ) {
        insertAlbum(album.album)
        insertTags(album.tags)
        insertTracks(album.tracks)
    }

    open fun getAlbum(id: AlbumId): Flow<AlbumToTagsAndTracks?> {
        return getAlbum(id.artist, id.album)
            .map {
                val album = it.firstOrNull() ?: return@map null
                getAlbumToTagsAndTracks(album)
            }
    }

    @Transaction
    protected open suspend fun getAlbumToTagsAndTracks(album: AlbumEntity): AlbumToTagsAndTracks {
        return AlbumToTagsAndTracks(
            album = album,
            tags = getTagsByAlbum(album.artist, album.name),
            tracks = getTracksByAlbum(album.artist, album.name)
        )
    }

    @Query("DELETE  FROM albums WHERE artist = :artist AND name = :album")
    abstract suspend fun delete(artist: String, album: String)

    @Query("SELECT * FROM albums WHERE artist = :artist AND name = :album")
    protected abstract fun getAlbum(artist: String, album: String): Flow<List<AlbumEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insertAlbum(album: AlbumEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insertTags(tags: List<AlbumEntity.TagEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insertTracks(tracks: List<AlbumEntity.TrackEntity>)

    @Query("SELECT * FROM albums")
    protected abstract fun getAllAlbums(): Flow<List<AlbumEntity>>

    @Query("SELECT * FROM tags WHERE albumArtist = :albumArtist AND albumName = :albumName")
    protected abstract suspend fun getTagsByAlbum(albumArtist: String, albumName: String): List<AlbumEntity.TagEntity>

    @Query("SELECT * FROM tracks WHERE albumArtist = :albumArtist AND albumName = :albumName")
    protected abstract suspend fun getTracksByAlbum(albumArtist: String, albumName: String): List<AlbumEntity.TrackEntity>
}