package me.kolotilov.lastfm_saver.repositories.persistance.repositories

import android.util.Log
import me.kolotilov.lastfm_saver.domain.CancelableAction
import me.kolotilov.lastfm_saver.models.AlbumId
import me.kolotilov.lastfm_saver.repositories.Repository

class DeleteAlbumAction(
    private val repository: Repository,
    private val albumId: AlbumId
) : CancelableAction {

    override suspend fun perform() {
        Log.e("BRUH", "perform()")
        repository.softDelete(albumId)
    }

    override suspend fun cancel() {
        repository.revertSoftDelete(albumId)
    }
}