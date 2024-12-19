package com.vn.thehiveshop.ui.cart

import androidx.lifecycle.ViewModelProvider
import com.vn.thehiveshop.data.repository.CartRepository

class CartViewModelFactory(private val cartRepository: CartRepository) : ViewModelProvider.Factory {
    // Factory cho ViewModel, cho phép khởi tạo với CartRepository
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CartViewModel(cartRepository) as T
        }

        throw IllegalArgumentException("Unable construct viewModel")
    }
}