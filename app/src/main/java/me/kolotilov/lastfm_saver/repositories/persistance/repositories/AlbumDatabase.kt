package me.kolotilov.lastfm_saver.repositories.persistance.repositories

import androidx.room.Database
import androidx.room.RoomDatabase
import me.kolotilov.lastfm_saver.repositories.persistance.entities.AlbumEntity

@Database(
    entities = [AlbumEntity::class, AlbumEntity.TagEntity::class, AlbumEntity.TrackEntity::class],
    version = 1
)
abstract class AlbumDatabase : RoomDatabase() {

    abstract val albumsDao: AlbumsDao
}