package com.ibnu.jelajahin.core.data.source.factory

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ibnu.jelajahin.core.data.model.Restaurant
import com.ibnu.jelajahin.core.data.remote.network.RestaurantService
import com.ibnu.jelajahin.core.utils.JelajahinConstValues
import java.util.*

class RestaurantPagingFactory(
    private val service: RestaurantService,
    private val provinceId: Int,
    private val searchQuery: String?
) : PagingSource<Int, Restaurant>() {

    override fun getRefreshKey(state: PagingState<Int, Restaurant>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Restaurant> {
        val result = service.getRestaurant(provinceId, searchQuery)

        val page = params.key ?: JelajahinConstValues.DEFAULT_PAGE_INDEX
        return LoadResult.Page(
            data = result.restaurants ?: Collections.emptyList(),
            nextKey = if ((result.rowCount / JelajahinConstValues.DEFAULT_PAGE_SIZE) < page) null else page + 1,
            prevKey = if (page == JelajahinConstValues.DEFAULT_PAGE_INDEX) null else page - 1,
        )
    }
}