package com.ibnu.jelajahin.core.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ibnu.jelajahin.core.data.model.Event
import com.ibnu.jelajahin.core.data.remote.network.EventService
import com.ibnu.jelajahin.core.utils.JelajahinConstValues.DEFAULT_PAGE_INDEX
import com.ibnu.jelajahin.core.utils.JelajahinConstValues.DEFAULT_PAGE_SIZE

class EventPagingFactory(
    private val service: EventService,
    private val provinceId: Int,
    private val cityId: Int
) : PagingSource<Int, Event>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Event> {
        val result = service.getEventByProvinceAndCity(provinceId, cityId)
        val page = params.key ?: DEFAULT_PAGE_INDEX
        return LoadResult.Page(
            data = result.events,
            nextKey = if ((result.rowCount / DEFAULT_PAGE_SIZE) < page) null else page + 1,
            prevKey = if (page == DEFAULT_PAGE_INDEX) null else page - 1,
        )
    }

    override fun getRefreshKey(state: PagingState<Int, Event>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}