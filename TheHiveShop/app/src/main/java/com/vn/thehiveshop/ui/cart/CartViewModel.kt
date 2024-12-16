package com.vn.thehiveshop.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.vn.thehiveshop.data.repository.CartRepository
import com.vn.thehiveshop.data.request.CartRequest
import com.vn.thehiveshop.utils.Resource
import kotlinx.coroutines.Dispatchers

class CartViewModel(private val cartRepository: CartRepository) : ViewModel() {

    private fun <T> executeRequest(request: suspend () -> T) = liveData(Dispatchers.IO) {
        emit(Resource.Loading(null))
        try {
            emit(Resource.Success(request()))
        } catch (e: Exception) {
            emit(Resource.Error(null, e.message ?: "Error"))
        }
    }

    fun addProductToCart(request: CartRequest) = executeRequest {
        cartRepository.addProductToCart(request)
    }

    fun getCart() = executeRequest {
        cartRepository.getCart().items
    }

    fun updateItemAmount(request: CartRequest) = executeRequest {
        cartRepository.updateItemAmount(request)
    }

    fun deleteItem(request: CartRequest) = executeRequest {
        cartRepository.deleteItem(request)
    }
}
