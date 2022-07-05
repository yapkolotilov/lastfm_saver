package me.kolotilov.lastfm_saver.ui.common

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import me.kolotilov.lastfm_saver.R

fun DividerItemDecoration(context: Context): DividerItemDecoration {
    return DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        .apply {
            setDrawable(ContextCompat.getDrawable(context, R.drawable.divider)!!)
        }
}