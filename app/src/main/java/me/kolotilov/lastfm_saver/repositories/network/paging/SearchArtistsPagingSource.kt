package me.kolotilov.lastfm_saver.repositories.network.paging

import me.kolotilov.lastfm_saver.models.Artist
import me.kolotilov.lastfm_saver.repositories.Repository
import me.kolotilov.lastfm_saver.repositories.common.BasePagingSource
import me.kolotilov.lastfm_saver.ui.common.recycler.EmptyPagingException
import me.kolotilov.lastfm_saver.ui.common.recycler.NotFoundPagingException

data class SearchArtistsPagingSource(
    private val repository: Repository,
    private val query: String
) : BasePagingSource<Artist>() {

    class Provider(
        private val repository: Repository
    ) : BasePagingSource.Provider<SearchArtistsPagingSource>() {

        var query = ""
            private set

        override fun newInstance(): SearchArtistsPagingSource {
            return SearchArtistsPagingSource(repository, query)
        }

        fun invalidate(query: String) {
            this.query = query
            invalidate()
        }
    }

    override suspend fun performRequest(page: Int, size: Int): Response<Artist> {
        if (query.isBlank())
            throw EmptyPagingException()

        val response = repository.searchArtists(query, page, size)
        if (response.artists.isEmpty())
            throw NotFoundPagingException()

        return Response(
            items = response.artists,
            totalSize = response.totalSize
        )
    }
}