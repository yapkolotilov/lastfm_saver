package me.kolotilov.lastfm_saver.screens

import com.agoda.kakao.recycler.KRecyclerView
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.toolbar.KToolbar
import me.kolotilov.lastfm_saver.R

object ArtistAlbumsScreen : Screen<ArtistAlbumsScreen>() {

    val toolbar = KToolbar { withId(R.id.toolbar) }
    val recycler = KRecyclerView({ withId(R.id.recycler) }, { itemType(::AlbumItem) })
}