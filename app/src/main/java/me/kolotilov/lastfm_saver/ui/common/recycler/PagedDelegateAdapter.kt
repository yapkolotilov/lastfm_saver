package me.kolotilov.lastfm_saver.ui.common.recycler

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import me.kolotilov.lastfm_saver.ui.common.AdapterDelegate

/**
 * Paged Recycler adapter.
 *
 * @param diffCallBack Diff util.
 * @param delegates Delegates.
 */
class PagedDelegateAdapter<T : Any>(
    diffCallBack: DiffUtil.ItemCallback<T>,
    vararg delegates: AdapterDelegate<T>
) : PagingDataAdapter<T, AdapterDelegate.ViewHolder<T>>(diffCallBack) {

    private val delegateManager = DelegateManager(*delegates)

    override fun getItemViewType(position: Int): Int = delegateManager.getItemViewType(getItem(position)!!)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterDelegate.ViewHolder<T> {
        return delegateManager.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: AdapterDelegate.ViewHolder<T>, position: Int) {
        delegateManager.onBindViewHolder(holder, getItem(position)!!, position)
    }
}