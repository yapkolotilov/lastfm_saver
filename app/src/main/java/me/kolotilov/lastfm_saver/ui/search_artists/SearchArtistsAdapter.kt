package me.kolotilov.lastfm_saver.ui.search_artists

import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import me.kolotilov.lastfm_saver.R
import me.kolotilov.lastfm_saver.databinding.ItemFoundArtistBinding
import me.kolotilov.lastfm_saver.presentation.common.ListItem
import me.kolotilov.lastfm_saver.ui.common.AdapterDelegate
import me.kolotilov.lastfm_saver.ui.common.adapterDelegate
import me.kolotilov.lastfm_saver.ui.common.recycler.PagedDelegateAdapter
import me.kolotilov.lastfm_saver.ui.utils.shimmerDrawable

fun searchArtistsAdapter(
    context: Context,
    onArtistClick: (ArtistItem) -> Unit
): PagedDelegateAdapter<ListItem> {
    return PagedDelegateAdapter(
        DiffUtilCallback(),

        artistItem(context, onArtistClick)
    )
}

private class DiffUtilCallback : DiffUtil.ItemCallback<ListItem>() {

    override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
        return when {
            oldItem is ArtistItem && newItem is ArtistItem -> oldItem.name == newItem.name
            else -> error("Illegal item type: ${oldItem::class}")
        }
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
        return oldItem == newItem
    }
}

private fun artistItem(
    context: Context,
    onClick: (ArtistItem) -> Unit
): AdapterDelegate<ListItem> {
    return adapterDelegate<ListItem, ArtistItem, ItemFoundArtistBinding>(
        inflate = ItemFoundArtistBinding::inflate,
        initView = { d ->
            root.setOnClickListener { onClick(d.item) }
        },
        bind = { item ->
            nameText.text = item.name
            listenersText.text =
                context.getString(R.string.item_search_artists_item_listeners, item.listeners)
            Glide.with(context)
                .load(item.imageUri)
                .placeholder(shimmerDrawable())
                .error(R.drawable.ic_error)
                .into(iconImage)
        }
    )
}