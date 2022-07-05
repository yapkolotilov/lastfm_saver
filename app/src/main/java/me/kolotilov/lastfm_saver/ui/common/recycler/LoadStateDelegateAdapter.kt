package me.kolotilov.lastfm_saver.ui.common.recycler

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import me.kolotilov.lastfm_saver.ui.common.AdapterDelegate

/**
 * Load State Adapter.
 *
 * @param delegates Delegates.
 */
class LoadStateDelegateAdapter(
    vararg delegates: AdapterDelegate<LoadState>
) : LoadStateAdapter<AdapterDelegate.ViewHolder<LoadState>>() {

    private val delegateManager = DelegateManager(*delegates)

    override fun onBindViewHolder(holder: AdapterDelegate.ViewHolder<LoadState>, loadState: LoadState) {
        return delegateManager.onBindViewHolder(holder, loadState, 0)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): AdapterDelegate.ViewHolder<LoadState> {
        return delegateManager.onCreateViewHolder(parent, delegateManager.getItemViewType(loadState))
    }
}