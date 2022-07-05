package me.kolotilov.lastfm_saver.ui.navigation

import androidx.navigation.NavController
import me.kolotilov.lastfm_saver.NavigationDirections.Companion.actionGlobalAlbumDetailsFragment
import me.kolotilov.lastfm_saver.models.AlbumId
import me.kolotilov.lastfm_saver.ui.saved_albums.SavedAlbumsFragmentDirections.Companion.actionSavedAlbumsFragmentToSearchArtistsFragment
import me.kolotilov.lastfm_saver.ui.saved_albums.SavedAlbumsFragmentDirections.Companion.actionSavedAlbumsFragmentToStoragePermissionNotGrantedFragment
import me.kolotilov.lastfm_saver.ui.search_artists.SearchArtistsFragmentDirections.Companion.actionSearchArtistsFragmentToArtistAlbumsFragment

/**
 * Routing helper.
 */
@JvmInline
value class Router(
    private val navController: NavController
) {

    /**
     * Shows search albums screen.
     */
    fun showSearchAlbums() {
        navController.navigate(actionSavedAlbumsFragmentToSearchArtistsFragment())
    }

    /**
     * Shows artist albums screen.
     *
     * @param artist Artist name.
     */
    fun showArtistAlbums(artist: String) {
        navController.navigate(actionSearchArtistsFragmentToArtistAlbumsFragment(artist))
    }

    /**
     * Shows album details screen.
     *
     * @param id Album ID.
     */
    fun showAlbumDetails(id: AlbumId) {
        navController.navigate(actionGlobalAlbumDetailsFragment(id.artist, id.album))
    }

    /**
     * Shows storage permission not granted screen.
     */
    fun showStoragePermissionNotGrantedDialog() {
        navController.navigate(actionSavedAlbumsFragmentToStoragePermissionNotGrantedFragment())
    }
}