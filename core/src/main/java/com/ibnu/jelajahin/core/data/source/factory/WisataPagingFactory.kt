package com.ibnu.jelajahin.core.data.source.factory

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ibnu.jelajahin.core.data.model.Wisata
import com.ibnu.jelajahin.core.data.remote.network.WisataService
import com.ibnu.jelajahin.core.utils.JelajahinConstValues

class WisataPagingFactory(
    private val service: WisataService,
    private val provinceId: Int,
    private val cityId: Int
) : PagingSource<Int, Wisata>() {

    override fun getRefreshKey(state: PagingState<Int, Wisata>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Wisata> {
        val result = service.getWisataByProvinceAndCity(provinceId, cityId)
        val page = params.key ?: JelajahinConstValues.DEFAULT_PAGE_INDEX
        return LoadResult.Page(
            data = result.wisata,
            nextKey = if ((result.rowCount / JelajahinConstValues.DEFAULT_PAGE_SIZE) < page) null else page + 1,
            prevKey = if (page == JelajahinConstValues.DEFAULT_PAGE_INDEX) null else page - 1,
        )
    }
}