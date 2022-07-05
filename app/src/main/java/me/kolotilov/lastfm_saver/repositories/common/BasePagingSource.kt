package me.kolotilov.lastfm_saver.repositories.common

import androidx.paging.PagingSource
import androidx.paging.PagingState

const val PAGE_SIZE = 30

/**
 * Base paging source implementation.
 */
abstract class BasePagingSource<T : Any>(
    private val pageSize: Int = PAGE_SIZE
) : PagingSource<Int, T>() {

    abstract class Provider<P : PagingSource<Int, *>> {

        private var _instance: P? = null

        protected abstract fun newInstance(): P

        fun create(): P {
            _instance = newInstance()
            return _instance!!
        }

        protected fun invalidate() {
            _instance?.invalidate()
        }
    }

    protected data class Response<T>(
        val items: List<T>,
        val totalSize: Int
    )

    /**
     * Performs request to network.
     */
    protected abstract suspend fun performRequest(page: Int, size: Int): Response<T>

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        return try {
            val page = params.key ?: 1
            val response = performRequest(page, pageSize)
            LoadResult.Page(
                data = response.items,
                prevKey = if (page != 1) page - 1 else null,
                nextKey = if (page * pageSize < response.totalSize) page + 1 else null
            )
        } catch (e: Throwable) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}