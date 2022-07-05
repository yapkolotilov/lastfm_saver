package me.kolotilov.lastfm_saver.ui.common.recycler

import android.view.ViewGroup
import me.kolotilov.lastfm_saver.ui.common.AdapterDelegate

class DelegateManager<T : Any>(
    private val adapters: List<AdapterDelegate<T>>
) {

    constructor(vararg adapters: AdapterDelegate<T>) : this(adapters.toList())

    fun getItemViewType(item: T): Int {
        return adapters.indexOfFirst { it.matches(item) }
    }

    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterDelegate.ViewHolder<T> {
        return adapters[viewType].createViewHolder(parent)
    }

    fun onBindViewHolder(holder: AdapterDelegate.ViewHolder<T>, item: T, position: Int) {
        holder.bind(item, position)
    }
}