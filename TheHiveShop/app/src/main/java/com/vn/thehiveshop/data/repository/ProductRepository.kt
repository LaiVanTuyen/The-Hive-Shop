package com.vn.thehiveshop.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.vn.thehiveshop.api.HiveService
import com.vn.thehiveshop.data.ProductPagingSource
import com.vn.thehiveshop.model.Product
import kotlinx.coroutines.flow.Flow


class ProductRepository(private val hiveService: HiveService) {

    // Function to get paginated products
    fun getAllProducts(): Flow<PagingData<Product>> = Pager(
        config = PagingConfig(
            pageSize = NETWORK_PAGE_SIZE,  // Configure how many items per page
            enablePlaceholders = false  // Disable placeholders for a smoother experience
        ),
        pagingSourceFactory = { ProductPagingSource(hiveService) }  // Use the service directly
    ).flow

    // Function to get all products without pagination
    suspend fun getAllProduct(): List<Product> {
        val response = hiveService.getAllProductsWithoutPaging()
        return if (response.isSuccessful) {
            response.body() ?: emptyList() // Trả về danh sách nếu thành công, nếu không trả về rỗng
        } else {
            throw Exception("Error: ${response.code()}") // Thông báo lỗi nếu không thành công
        }
    }


    companion object {
        const val NETWORK_PAGE_SIZE = 10
    }
}