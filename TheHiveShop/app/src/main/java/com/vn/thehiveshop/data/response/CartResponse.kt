package com.vn.thehiveshop.data.response

import com.vn.thehiveshop.model.ItemInCart

data class CartResponse(
    val email:String,
    val items:List<ItemInCart>
)