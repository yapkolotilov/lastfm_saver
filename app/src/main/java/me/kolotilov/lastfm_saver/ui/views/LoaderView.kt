package me.kolotilov.lastfm_saver.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import me.kolotilov.lastfm_saver.databinding.ViewLoaderBinding

/**
 * Loader View.
 */
class LoaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = -1,
    defStyleRes: Int = -1
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    init {
        ViewLoaderBinding.inflate(LayoutInflater.from(context), this)
    }
}