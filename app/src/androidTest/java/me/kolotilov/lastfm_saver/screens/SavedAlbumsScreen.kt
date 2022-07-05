package me.kolotilov.lastfm_saver.screens

import android.view.View
import com.agoda.kakao.common.views.KView
import com.agoda.kakao.recycler.KRecyclerItem
import com.agoda.kakao.recycler.KRecyclerView
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KButton
import com.agoda.kakao.toolbar.KToolbar
import me.kolotilov.lastfm_saver.R
import org.hamcrest.Matcher

object SavedAlbumsScreen : Screen<SavedAlbumsScreen>() {

    val toolbar = KToolbar { withId(R.id.toolbar) }

    val recycler = KRecyclerView(
        { withId(R.id.recycler) },
        {
            itemType(::AlbumItem)
        }
    )

    val searchButton = KButton { withId(R.id.search_button) }

    val placeholder = KView { withId(R.id.placeholder) }
}

class AlbumItem(parent: Matcher<View>) : KRecyclerItem<AlbumItem>(parent) {

    val saveButton = KButton(parent) { withId(R.id.save_button) }
    val nameText = KButton(parent) { withId(R.id.album_name_text) }
    val artistText = KButton(parent) { withId(R.id.artist_name_text) }
}