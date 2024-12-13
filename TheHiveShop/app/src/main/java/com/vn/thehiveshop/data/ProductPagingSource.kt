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
        // Tính toán khóa refresh để đảm bảo người dùng trở lại trang gần nhất khi làm mới
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    /*override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        val position = params.key ?: PRODUCT_STARTING_INDEX
        return try {
            val products = hiveService.getAllProducts(position, params.loadSize)
            LoadResult.Page(
                prevKey = if (position == PRODUCT_STARTING_INDEX) null else position - 1,
                nextKey = if (products.body()?.isEmpty() == true) null else position + (params.loadSize / ProductRepository.NETWORK_PAGE_SIZE),
                data = products.body() ?: emptyList()
            )
        } catch (ex: IOException) {
            LoadResult.Error(ex)
        } catch (ex: HttpException) {
            LoadResult.Error(ex)
        }
    }*/

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        val position = params.key ?: PRODUCT_STARTING_INDEX // Mặc định trang bắt đầu
        return try {
            // Gọi API để lấy danh sách sản phẩm
            val response = hiveService.getAllProducts(position, params.loadSize)

            if (response.isSuccessful) {
                val products = response.body().orEmpty() // Nếu body là null, trả về danh sách rỗng
                LoadResult.Page(
                    data = products,
                    prevKey = if (position == PRODUCT_STARTING_INDEX) null else position - 1,
                    nextKey = if (products.isEmpty()) null else position + 1 // Nếu hết dữ liệu, nextKey = null
                )
            } else {
                LoadResult.Error(HttpException(response)) // Xử lý khi API trả về lỗi HTTP
            }
        } catch (e: IOException) {
            LoadResult.Error(e) // Xử lý lỗi kết nối mạng
        } catch (e: HttpException) {
            LoadResult.Error(e) // Xử lý lỗi HTTP (ngoại lệ từ Retrofit)
        }
    }


}