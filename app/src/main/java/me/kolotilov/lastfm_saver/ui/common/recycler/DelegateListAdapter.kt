package me.kolotilov.lastfm_saver.ui.common.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import me.kolotilov.lastfm_saver.ui.common.AdapterDelegate

class DelegateListAdapter<T : Any>(
    diffUtilCallback: DiffUtil.ItemCallback<T>,
    vararg delegates: AdapterDelegate<T>
) : ListAdapter<T, AdapterDelegate.ViewHolder<T>>(diffUtilCallback) {

    private val manager = DelegateManager(*delegates)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterDelegate.ViewHolder<T> = manager.onCreateViewHolder(parent, viewType)

    override fun onBindViewHolder(holder: AdapterDelegate.ViewHolder<T>, position: Int) =
        manager.onBindViewHolder(holder, getItem(position), position)

    override fun getItemViewType(position: Int): Int = manager.getItemViewType(getItem(position))
}