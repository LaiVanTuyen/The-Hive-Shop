package com.vn.thehiveshop.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.vn.thehiveshop.api.HiveService
import com.vn.thehiveshop.data.repository.ProductRepository
import com.vn.thehiveshop.model.Product
import retrofit2.HttpException
import java.io.IOException

private const val PRODUCT_STARTING_INDEX = 1

class ProductPagingSource(
    private val hiveService: HiveService
): PagingSource<Int, Product>() {
    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        val position = params.key ?: PRODUCT_STARTING_INDEX
        return try {
            val products = hiveService.getAllProducts(position, params.loadSize)
            LoadResult.Page(
                prevKey = if (position == PRODUCT_STARTING_INDEX) null else position - 1,
                nextKey = if (products.isEmpty()) null else position + (params.loadSize / ProductRepository.NETWORK_PAGE_SIZE),
                data = products
            )
        } catch (ex: IOException) {
            LoadResult.Error(ex)
        } catch (ex: HttpException) {
            LoadResult.Error(ex)
        }
    }


}