package me.kolotilov.lastfm_saver.utils

import android.util.Log

/**
 * Application logger
 *
 * @param tag Tag.
 */
@JvmInline
value class Logger(
    private val tag: String
) {

    /**
     * Creates new application logger.
     *
     * @param receiver this-reference.
     */
    constructor(receiver: Any) : this(receiver::class.simpleName.toString())

    /**
     * Prints debug-level message.
     *
     * @param message Message.
     */
    fun debug(message: Any?) {
        Log.d(tag, message.toString())
    }

    /**
     * Prints error-level message.
     *
     * @param message Message.
     */
    fun error(message: Any?) {
        Log.e(tag, message.toString())
    }

    fun tag(tag: String): Logger {
        return Logger(tag)
    }
}