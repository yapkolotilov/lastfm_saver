package me.kolotilov.lastfm_saver.common

import me.kolotilov.lastfm_saver.models.AlbumDetails
import me.kolotilov.lastfm_saver.models.Artist

object Mock {

    private val baseArtist = Artist(
        name = "",
        listeners = 0,
        imageUri = "",
        uri = ""
    )

    private val baseAlbum = AlbumDetails(
        artist = "",
        name = "",
        image = null,
        playCount = 0,
        tags = emptyList(),
        tracks = emptyList(),
        uri = ""
    )

    private val baseTrack = AlbumDetails.Track(
        name = "",
        durationSecs = null,
        uri = ""
    )

    private val tracks = listOf(
        baseTrack.copy(
            name = "Believe",
            durationSecs = 130
        ),
        baseTrack.copy(
            name = "Waterloo",
            durationSecs = 130
        ),
        baseTrack.copy(
            name = "All or nothing",
            durationSecs = 130
        )
    )

    val artists = listOf(
        baseArtist.copy(
            name = "Cher",
            listeners = 12_412
        ),
        baseArtist.copy(
            name = "My Chemical Romance",
            listeners = 1540
        ),
        baseArtist.copy(
            name = "Cherry lady",
            listeners = 130
        )
    )

    val albums = listOf(
        baseAlbum.copy(
            name = "Believe",
            artist = "Cher",
            playCount = 126,
            tracks = tracks
        ),
        baseAlbum.copy(
            name = "Best of Cher",
            artist = "Cher",
            playCount = 154,
            tracks = tracks
        ),
        baseAlbum.copy(
            name = "Cover songs",
            artist = "Cher",
            playCount = 1045,
            tracks = tracks
        )
    )
}