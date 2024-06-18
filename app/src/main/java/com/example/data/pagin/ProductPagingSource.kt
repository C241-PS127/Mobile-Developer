package com.example.data.pagin

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.data.api.ApiService
import com.example.data.response.ProductsItem
import kotlin.random.Random

class ProductPagingSource(
    private val apiService: ApiService
) : PagingSource<Int, ProductsItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductsItem> {
        return try {
            val randomInt = Random.nextInt(1, 31)
            val currentPage = params.key ?: randomInt
            val response = apiService.getAllProducts(currentPage, params.loadSize)
            val products = response.products

            LoadResult.Page(
                data = products,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (products.isEmpty()) null else currentPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ProductsItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}

