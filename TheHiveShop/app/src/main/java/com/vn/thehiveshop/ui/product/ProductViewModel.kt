package com.vn.thehiveshop.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.vn.thehiveshop.data.repository.ProductRepository
import com.vn.thehiveshop.model.Product
import kotlinx.coroutines.flow.Flow

class ProductViewModel : ViewModel() {
    private val productRepository: ProductRepository = ProductRepository()

    fun getAllProduct(): Flow<PagingData<Product>> {
        return productRepository.getAllProducts().cachedIn(viewModelScope)
    }

    class ProductViewModelFactory : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ProductViewModel() as T
            }
            throw IllegalArgumentException("Unable to construct viewModel")
        }
    }
}
