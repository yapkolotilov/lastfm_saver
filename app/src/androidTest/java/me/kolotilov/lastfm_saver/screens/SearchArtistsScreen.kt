package me.kolotilov.lastfm_saver.screens

import android.view.View
import com.agoda.kakao.common.views.KView
import com.agoda.kakao.edit.KTextInputLayout
import com.agoda.kakao.recycler.KRecyclerItem
import com.agoda.kakao.recycler.KRecyclerView
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KButton
import com.agoda.kakao.text.KTextView
import com.agoda.kakao.toolbar.KToolbar
import me.kolotilov.lastfm_saver.R
import org.hamcrest.Matcher

object SearchArtistsScreen : Screen<SearchArtistsScreen>() {

    val toolbar = KToolbar { withId(R.id.toolbar) }
    val searchInput = KTextInputLayout { withId(R.id.search_input) }
    val searchButton = KButton { withId(R.id.search_button) }
    val recycler = KRecyclerView({ withId(R.id.recycler) }, { itemType(::ArtistItem) })
    val loader = KView { withId(R.id.loader) }
    val placeholder = KView { withId(R.id.placeholder) }
    val errorView = KView { withId(R.id.error) }
}

class ArtistItem(parent: Matcher<View>) : KRecyclerItem<ArtistItem>(parent) {

    val nameText = KTextView(parent) { withId(R.id.name_text) }
    val listenersText = KTextView(parent) { withId(R.id.listeners_text) }
}