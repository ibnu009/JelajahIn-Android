package com.ibnu.jelajahin.core.data.source.factory

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ibnu.jelajahin.core.data.model.PointHistory
import com.ibnu.jelajahin.core.data.remote.network.UserService
import com.ibnu.jelajahin.core.utils.JelajahinConstValues
import com.ibnu.jelajahin.core.utils.JelajahinConstValues.DEFAULT_PAGE_SIZE

class HistoryPointPagingFactory(
    private val service: UserService,
    private val token: String
) : PagingSource<Int, PointHistory>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PointHistory> {
        val result = service.getUserPointHistory(token)
        val data = result.histories ?: emptyList()
        val page = params.key ?: JelajahinConstValues.DEFAULT_PAGE_INDEX
        return LoadResult.Page(
            data = data,
            nextKey = if ((result.rowCount / DEFAULT_PAGE_SIZE) < page) null else page + 1,
            prevKey = if (page == JelajahinConstValues.DEFAULT_PAGE_INDEX) null else page - 1,
        )
    }

    override fun getRefreshKey(state: PagingState<Int, PointHistory>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}