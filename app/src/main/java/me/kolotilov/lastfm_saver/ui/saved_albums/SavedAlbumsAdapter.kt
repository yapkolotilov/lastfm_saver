package me.kolotilov.lastfm_saver.ui.saved_albums

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import me.kolotilov.lastfm_saver.presentation.common.ListItem
import me.kolotilov.lastfm_saver.ui.artist_albums.AlbumPreviewItem
import me.kolotilov.lastfm_saver.ui.artist_albums.albumDelegate
import me.kolotilov.lastfm_saver.ui.common.recycler.DelegateAdapter
import me.kolotilov.lastfm_saver.ui.common.recycler.DelegateListAdapter

fun savedAlbumsAdapter(
    onAlbumClick: (AlbumPreviewItem) -> Unit,
    onSaveClicked: (AlbumPreviewItem) -> Unit
): DelegateListAdapter<ListItem> {
    return DelegateListAdapter(
        DiffUtilCallback(),
        albumDelegate(
            onAlbumClick = onAlbumClick,
            onSaveClicked = onSaveClicked
        )
    )
}

private class DiffUtilCallback : DiffUtil.ItemCallback<ListItem>() {

    override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
        return when {
            oldItem is AlbumPreviewItem && newItem is AlbumPreviewItem -> oldItem.id() == newItem.id()
            else -> error("Illegal item!")
        }
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
        return when {
            oldItem is AlbumPreviewItem && newItem is AlbumPreviewItem -> oldItem == newItem
            else -> error("Illegal item!")
        }
    }
}