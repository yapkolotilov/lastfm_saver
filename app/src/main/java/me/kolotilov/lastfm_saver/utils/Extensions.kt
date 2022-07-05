package me.kolotilov.lastfm_saver.utils

operator fun <T, I : T> I.plus(other: List<T>): List<T> {
    return buildList {
        add(this@plus)
        addAll(other)
    }
}