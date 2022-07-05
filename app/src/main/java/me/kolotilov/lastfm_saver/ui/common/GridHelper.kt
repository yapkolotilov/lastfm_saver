package me.kolotilov.lastfm_saver.ui.common

import android.app.Activity
import android.util.DisplayMetrics
import me.kolotilov.lastfm_saver.ui.utils.dp

/**
 * GridLayoutManager helper.
 */
@JvmInline
value class GridHelper(
    private val activity: Activity
) {

    /**
     * Returns Album grid span count.
     */
    @Suppress("DEPRECATION")
    fun albumGridSize(): Int {
        val displaymetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displaymetrics)
        val screenWidth = displaymetrics.widthPixels
        return screenWidth / (160 + 2 * 8).dp(activity)
    }
}