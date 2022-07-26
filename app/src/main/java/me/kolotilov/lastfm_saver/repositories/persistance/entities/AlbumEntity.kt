package me.kolotilov.lastfm_saver.repositories.persistance.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import me.kolotilov.lastfm_saver.models.AlbumDetails
import me.kolotilov.lastfm_saver.models.AlbumId

@Entity(
    tableName = "albums",
    primaryKeys = ["name", "artist"]
)
data class AlbumEntity(
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "artist")
    val artist: String,
    @ColumnInfo(name = "imageUri")
    val imageUri: String?,
    @ColumnInfo(name = "imagePath")
    val imagePath: String?,
    @ColumnInfo(name = "playCount")
    val playCount: Int,
    @ColumnInfo(name = "uri")
    val uri: String,
    @ColumnInfo(name = "deleted")
    val deleted: Boolean = false
) {

    @Entity(
        tableName = "tags",
        foreignKeys = [
            ForeignKey(
                entity = AlbumEntity::class,
                parentColumns = ["name", "artist"],
                childColumns = ["albumName", "albumArtist"],
                onDelete = ForeignKey.CASCADE
            )
        ]
    )
    data class TagEntity(
        @PrimaryKey
        @ColumnInfo(name = "name")
        val name: String,
        @ColumnInfo(name = "albumName")
        val albumName: String,
        @ColumnInfo(name = "albumArtist")
        val albumArtist: String,
        @ColumnInfo(name = "uri")
        val uri: String
    )

    @Entity(
        tableName = "tracks",
        primaryKeys = ["name", "albumName", "albumArtist"],
        foreignKeys = [
            ForeignKey(
                entity = AlbumEntity::class,
                parentColumns = ["name", "artist"],
                childColumns = ["albumName", "albumArtist"],
                onDelete = ForeignKey.CASCADE
            )
        ]
    )
    data class TrackEntity(
        @ColumnInfo(name = "name")
        val name: String,
        @ColumnInfo(name = "albumName")
        val albumName: String,
        @ColumnInfo(name = "albumArtist")
        val albumArtist: String,
        @ColumnInfo(name = "durationSecs")
        val durationSecs: Int?,
        @ColumnInfo(name = "uri")
        val uri: String,
    )
}

fun AlbumDetails.toAlbumEntity(): AlbumEntity {
    return AlbumEntity(
        artist = artist,
        imagePath = image?.filePath,
        imageUri = image?.uri,
        name = name,
        playCount = playCount,
        uri = uri,
    )
}

fun AlbumDetails.Track.toTrackEntity(albumId: AlbumId): AlbumEntity.TrackEntity {
    return AlbumEntity.TrackEntity(
        albumArtist = albumId.artist,
        albumName = albumId.album,
        name = name,
        uri = uri,
        durationSecs = durationSecs,
    )
}

fun AlbumDetails.Tag.toTagEntity(albumId: AlbumId): AlbumEntity.TagEntity {
    return AlbumEntity.TagEntity(
        name = name,
        albumArtist = albumId.artist,
        albumName = albumId.album,
        uri = uri,
    )
}

fun AlbumEntity.TagEntity.toTag(): AlbumDetails.Tag {
    return AlbumDetails.Tag(
        name = name,
        uri = uri
    )
}

fun AlbumEntity.TrackEntity.toTrack(): AlbumDetails.Track {
    return AlbumDetails.Track(
        durationSecs = durationSecs,
        uri = uri,
        name = name
    )
}