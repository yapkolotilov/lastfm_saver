package me.kolotilov.lastfm_saver.repositories.network.paging

import me.kolotilov.lastfm_saver.models.AlbumPreview
import me.kolotilov.lastfm_saver.repositories.Repository
import me.kolotilov.lastfm_saver.repositories.common.BasePagingSource
import me.kolotilov.lastfm_saver.ui.common.recycler.NotFoundPagingException

class ArtistAlbumsPagingSource(
    private val repository: Repository,
    private val artist: String
) : BasePagingSource<AlbumPreview>() {

    class Provider(
        private val repository: Repository
    ) : BasePagingSource.Provider<ArtistAlbumsPagingSource>() {

        private var _artist: String = ""

        override fun newInstance(): ArtistAlbumsPagingSource {
            return ArtistAlbumsPagingSource(repository, _artist)
        }

        fun invalidate(artist: String) {
            _artist = artist
            invalidate()
        }
    }

    override suspend fun performRequest(page: Int, size: Int): Response<AlbumPreview> {
        val response = repository.getArtistAlbums(artist, page, size)
        if (response.albums.isEmpty())
            throw NotFoundPagingException()

        return Response(
            items = response.albums,
            totalSize = response.totalSize
        )
    }
}