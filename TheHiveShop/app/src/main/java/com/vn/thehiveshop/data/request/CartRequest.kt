package com.vn.thehiveshop.data.request

import com.vn.thehiveshop.model.ItemInCart
import com.google.gson.annotations.SerializedName
import com.vn.thehiveshop.data.User

data class CartRequest(
    @field:SerializedName("email") val email: String = User.email,
    @field:SerializedName("item") val item: ItemInCart
)