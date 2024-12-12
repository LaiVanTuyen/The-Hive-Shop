package com.vn.thehiveshop.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.vn.thehiveshop.api.HiveService
import com.vn.thehiveshop.data.ProductPagingSource
import com.vn.thehiveshop.model.Product
import kotlinx.coroutines.flow.Flow


class ProductRepository() {

    fun getAllProducts(): Flow<PagingData<Product>> = Pager(
        config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
        pagingSourceFactory = { ProductPagingSource(HiveService.create()) }
    ).flow


    companion object {
        const val NETWORK_PAGE_SIZE = 10
    }
}