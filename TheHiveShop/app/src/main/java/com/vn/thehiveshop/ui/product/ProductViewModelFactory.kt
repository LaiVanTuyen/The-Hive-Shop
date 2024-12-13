package com.vn.thehiveshop.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vn.thehiveshop.data.repository.ProductRepository

class ProductViewModelFactory (private val productRepository: ProductRepository) : ViewModelProvider.Factory {
    // Factory cho ViewModel, cho phép khởi tạo với ProductRepository
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            return ProductViewModel(productRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}