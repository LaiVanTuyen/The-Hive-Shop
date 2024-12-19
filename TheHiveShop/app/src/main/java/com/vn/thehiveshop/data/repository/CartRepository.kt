package com.vn.thehiveshop.data.repository

import com.vn.thehiveshop.api.HiveService
import com.vn.thehiveshop.data.User
import com.vn.thehiveshop.data.request.CartRequest

class CartRepository(private val hiveService: HiveService) {

        suspend fun addProductToCart(request: CartRequest) = hiveService.addProductToCart(request)

        suspend fun getCart() = hiveService.getCart(User.email)

        suspend fun updateItemAmount(request: CartRequest) = hiveService.updateItemAmount(request)

        suspend fun deleteItem(request: CartRequest) = hiveService.deleteItem(request)
}