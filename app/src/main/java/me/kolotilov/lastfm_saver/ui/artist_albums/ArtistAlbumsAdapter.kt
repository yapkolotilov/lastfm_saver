package me.kolotilov.lastfm_saver.ui.artist_albums

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import me.kolotilov.lastfm_saver.R
import me.kolotilov.lastfm_saver.databinding.ItemAlbumBinding
import me.kolotilov.lastfm_saver.presentation.common.ListItem
import me.kolotilov.lastfm_saver.ui.common.AdapterDelegate
import me.kolotilov.lastfm_saver.ui.common.adapterDelegate
import me.kolotilov.lastfm_saver.ui.common.recycler.PagedDelegateAdapter
import me.kolotilov.lastfm_saver.ui.utils.dp
import me.kolotilov.lastfm_saver.ui.utils.loadImage
import me.kolotilov.lastfm_saver.ui.utils.shimmerDrawable

fun artistAlbumsAdapter(
    onAlbumClick: (AlbumPreviewItem) -> Unit,
    onSaveClicked: (AlbumPreviewItem) -> Unit
): PagedDelegateAdapter<ListItem> {
    return PagedDelegateAdapter(
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
            else -> error("Unknown item type ${oldItem::class}")
        }
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
        return oldItem == newItem
    }
}

fun albumDelegate(
    onAlbumClick: (AlbumPreviewItem) -> Unit,
    onSaveClicked: (AlbumPreviewItem) -> Unit
): AdapterDelegate<ListItem> {
    return adapterDelegate<ListItem, AlbumPreviewItem, ItemAlbumBinding>(
        inflate = ItemAlbumBinding::inflate,
        initView = { d ->
            root.setOnClickListener {
                onAlbumClick(d.item)
            }
            root.setOnLongClickListener {
                val item = d.item
                item.saved = !item.saved
                d.invalidate()
                onSaveClicked(item)
                true
            }
            saveButton.setOnClickListener {
                val item = d.item
                item.saved = !item.saved
                d.invalidate()
                onSaveClicked(item)
            }
        },
        bind = { item ->
            Glide.with(root.context)
                .loadImage(item.image)
                .placeholder(shimmerDrawable())
                .error(R.drawable.ic_error)
                .override(160.dp(root.context), 160.dp(root.context))
                .into(albumImage)

            albumNameText.text = item.name
            artistNameText.text = item.artist
            playCountText.text = item.playCount

            val savedButtonText = if (item.saved) R.string.button_delete else R.string.button_save
            saveButton.setText(savedButtonText)
        }
    )
}