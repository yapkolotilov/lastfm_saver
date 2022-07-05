package me.kolotilov.lastfm_saver

import android.content.Context
import io.mockk.every
import io.mockk.mockk
import me.kolotilov.lastfm_saver.models.AlbumId
import me.kolotilov.lastfm_saver.repositories.common.FileHelper
import me.kolotilov.lastfm_saver.repositories.common.FileHelperImpl
import org.junit.Assert
import org.junit.Test
import java.io.File


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun fileHelper() {
        val context = mockk<Context> {
            every { filesDir } returns File("root")
        }
        val fileHelper: FileHelper = FileHelperImpl(context)
        val id = AlbumId("artist", "album")

        Assert.assertEquals(File("root", "artist_album.jpg"), fileHelper.imageFile(id))
    }
}