package me.kolotilov.lastfm_saver.repositories.persistance.repositories

import androidx.room.*
import me.kolotilov.lastfm_saver.models.AlbumId
import me.kolotilov.lastfm_saver.repositories.persistance.entities.AlbumEntity
import me.kolotilov.lastfm_saver.repositories.persistance.entities.AlbumToTagsAndTracks

@Dao
abstract class AlbumsDao {

    @Transaction
    open suspend fun getAll(): List<AlbumToTagsAndTracks> {
        return getAllAlbums().map {
            AlbumToTagsAndTracks(
                album = it,
                tags = getTagsByAlbum(it.artist, it.name),
                tracks = getTracksByAlbum(it.artist, it.name)
            )
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

    @Transaction
    open suspend fun getAlbum(id: AlbumId): AlbumToTagsAndTracks? {
        val album = getAlbum(id.artist, id.album).firstOrNull() ?: return null
        return AlbumToTagsAndTracks(
            album = album,
            tags = getTagsByAlbum(album.artist, album.name),
            tracks = getTracksByAlbum(album.artist, album.name)
        )
    }

    @Query("DELETE  FROM albums WHERE artist = :artist AND name = :album")
    abstract suspend fun delete(artist: String, album: String)


    @Query("SELECT * FROM albums WHERE artist = :artist AND name = :album")
    protected abstract suspend fun getAlbum(artist: String, album: String): List<AlbumEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insertAlbum(album: AlbumEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insertTags(tags: List<AlbumEntity.TagEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insertTracks(tracks: List<AlbumEntity.TrackEntity>)

    @Query("SELECT * FROM albums")
    protected abstract suspend fun getAllAlbums(): List<AlbumEntity>

    @Query("SELECT * FROM tags WHERE albumArtist = :albumArtist AND albumName = :albumName")
    protected abstract suspend fun getTagsByAlbum(albumArtist: String, albumName: String): List<AlbumEntity.TagEntity>

    @Query("SELECT * FROM tracks WHERE albumArtist = :albumArtist AND albumName = :albumName")
    protected abstract suspend fun getTracksByAlbum(albumArtist: String, albumName: String): List<AlbumEntity.TrackEntity>
}