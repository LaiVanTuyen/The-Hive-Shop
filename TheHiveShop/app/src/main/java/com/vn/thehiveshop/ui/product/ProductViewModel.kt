package com.vn.thehiveshop.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.vn.thehiveshop.data.repository.ProductRepository
import com.vn.thehiveshop.model.Product
import com.vn.thehiveshop.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException

//class ProductViewModel : ViewModel() {
//    private val productRepository: ProductRepository = ProductRepository()
class ProductViewModel(private val productRepository: ProductRepository) : ViewModel() {

    // Hàm lấy tất cả sản phẩm không phân trang
    fun getAllProduct() = liveData(Dispatchers.IO) {
        emit(Resource.Loading(null))
        try {
            val data = productRepository.getAllProduct()
            emit(Resource.Success(data))
        } catch (ex: IOException) {
            emit(Resource.Error(null, "Network error: ${ex.message}"))
        } catch (ex: HttpException) {
            emit(Resource.Error(null, "HTTP error: ${ex.message}"))
        }
    }

    // Trả về Flow<PagingData<Product>> và lưu trạng thái phân trang trong ViewModelScope
    fun getAllProducts(): Flow<PagingData<Product>> {
        return productRepository.getAllProducts()
            .cachedIn(viewModelScope)  // Lưu trạng thái phân trang trong ViewModelScope
    }


//    class ProductViewModelFactory : ViewModelProvider.Factory {
//        override fun <T : ViewModel> create(modelClass: Class<T>): T {
//            if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
//                @Suppress("UNCHECKED_CAST")
//                return ProductViewModel() as T
//            }
//            throw IllegalArgumentException("Unable to construct viewModel")
//        }
//    }


}
