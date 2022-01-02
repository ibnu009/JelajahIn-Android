package com.ibnu.jelajahin.core.data.source.factory

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ibnu.jelajahin.core.data.model.Penginapan
import com.ibnu.jelajahin.core.data.remote.network.PenginapanService
import com.ibnu.jelajahin.core.utils.JelajahinConstValues

class PenginapanPagingFactory(
    private val service: PenginapanService,
    private val provinceId: Int,
    private val searchQuery: String?
) : PagingSource<Int, Penginapan>() {

    override fun getRefreshKey(state: PagingState<Int, Penginapan>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Penginapan> {
        val result = service.getPenginapan(provinceId, searchQuery)

        val page = params.key ?: JelajahinConstValues.DEFAULT_PAGE_INDEX
        return LoadResult.Page(
            data = result.listPenginapan,
            nextKey = if ((result.rowCount / JelajahinConstValues.DEFAULT_PAGE_SIZE) < page) null else page + 1,
            prevKey = if (page == JelajahinConstValues.DEFAULT_PAGE_INDEX) null else page - 1,
        )
    }
}