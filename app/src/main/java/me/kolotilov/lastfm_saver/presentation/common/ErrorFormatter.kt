package me.kolotilov.lastfm_saver.presentation.common

import me.kolotilov.lastfm_saver.R
import java.net.UnknownHostException

@JvmInline
value class ErrorFormatter(
    private val resourceProvider: ResourceProvider
) {

    fun format(e: Throwable): String {
        return when(e) {
            is UnknownHostException -> resourceProvider.getString(R.string.snack_no_connection)
            else                    -> resourceProvider.getString(R.string.snack_unknown_error)
        }
    }
}