package me.kolotilov.lastfm_saver.presentation.common

import android.content.Context
import androidx.annotation.StringRes

/**
 * Presentation-layer resource provider for easier formatting.
 */
interface ResourceProvider {

    /**
     * Returns string.
     *
     * @param resId Resource.
     */
    fun getString(@StringRes resId: Int): String

    /**
     * Returns string.
     *
     * @param resId Resource.
     * @param args Arguments.
     */
    fun getString(@StringRes resId: Int, vararg args: Any): String
}

class ResourceProviderImpl(
    private val context: Context
) : ResourceProvider {

    override fun getString(resId: Int): String = context.getString(resId)

    override fun getString(resId: Int, vararg args: Any): String = context.getString(resId, *args)
}