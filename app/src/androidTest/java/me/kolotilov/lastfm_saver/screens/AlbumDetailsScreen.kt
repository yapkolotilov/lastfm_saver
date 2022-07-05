package me.kolotilov.lastfm_saver.screens

import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KButton
import com.agoda.kakao.text.KTextView
import com.agoda.kakao.toolbar.KToolbar
import me.kolotilov.lastfm_saver.R

object AlbumDetailsScreen : Screen<AlbumDetailsScreen>() {

    val toolbar = KToolbar { withId(R.id.toolbar) }
    val saveButton = KButton { withId(R.id.save_button) }
    val artistText = KTextView { withId(R.id.artist_name_text) }
}