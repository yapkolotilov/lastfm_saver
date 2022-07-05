package me.kolotilov.lastfm_saver.ui.common.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.kolotilov.lastfm_saver.ui.common.AdapterDelegate

class DelegateAdapter<T : Any>(
    vararg delegates: AdapterDelegate<T>
) : RecyclerView.Adapter<AdapterDelegate.ViewHolder<T>>() {

    var items: List<T> = emptyList()

    private val _manager = DelegateManager(*delegates)

    override fun getItemViewType(position: Int): Int {
        return _manager.getItemViewType(items[position])
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterDelegate.ViewHolder<T> {
        return _manager.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: AdapterDelegate.ViewHolder<T>, position: Int) {
        _manager.onBindViewHolder(holder, items[position], position)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}