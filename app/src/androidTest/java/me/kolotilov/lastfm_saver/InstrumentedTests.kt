package me.kolotilov.lastfm_saver

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import me.kolotilov.lastfm_saver.common.Mock
import me.kolotilov.lastfm_saver.common.MockRepositoryImpl
import me.kolotilov.lastfm_saver.repositories.Repository
import me.kolotilov.lastfm_saver.screens.*
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class InstrumentedTests : KoinComponent {

    @get:Rule
    val rules = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun searchArtists() {
        SavedAlbumsScreen {
            recycler.hasSize(0)
            searchButton.click()
        }
        SearchArtistsScreen {
            searchInput.edit.typeText("Cher")
            searchButton.click()
            recycler.hasSize(Mock.artists.size)
            recycler.childAt<ArtistItem>(0) {
                nameText.hasText(Mock.artists.first().name)
            }
        }
    }

    @Test
    fun saveAlbum() {
        searchArtists()
        SearchArtistsScreen {
            recycler.childAt<ArtistItem>(0) {
                click()
            }
        }
        ArtistAlbumsScreen {
            toolbar.hasTitle(Mock.artists.first().name)
            recycler.childAt<AlbumItem>(0) {
                nameText.hasText(Mock.albums.first().name)
                artistText.hasText(Mock.albums.first().artist)
                saveButton.hasText(R.string.button_save)
                saveButton.click()
                saveButton.hasText(R.string.button_delete)
            }
            pressBack()
        }
        SearchArtistsScreen {
            pressBack()
        }
        SavedAlbumsScreen {
            recycler {
                hasSize(1)
                childAt<AlbumItem>(0) {
                    nameText.hasText(Mock.albums.first().name)
                    artistText.hasText(Mock.albums.first().artist)
                }
            }
        }
    }

    @Test
    fun deleteAlbum() {
        saveAlbum()
        SavedAlbumsScreen {
            recycler {
                hasSize(1)
                childAt<AlbumItem>(0) {
                    saveButton.click()
                }
                hasSize(0)
            }
        }
    }

    @Test
    fun deleteAlbumFromDetails() {
        saveAlbum()
        SavedAlbumsScreen {
            recycler {
                childAt<AlbumItem>(0) {
                    nameText.click()
                }
            }
        }
        AlbumDetailsScreen {
            val album = Mock.albums.first()
            toolbar.hasTitle(album.name)
            artistText.hasText(album.artist)
            saveButton.hasText(R.string.button_delete)
            saveButton.click()
            saveButton.hasText(R.string.button_save)

            pressBack()
        }
        SavedAlbumsScreen {
            recycler.hasSize(0)
        }
    }

    @After
    fun clear() {
        get<Repository>().let { it as MockRepositoryImpl }.clear()
    }
}