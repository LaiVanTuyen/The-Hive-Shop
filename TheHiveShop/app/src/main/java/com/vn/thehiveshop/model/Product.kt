package com.vn.thehiveshop.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Product(
    @field:SerializedName("id")  val id: String,
    @field:SerializedName("title")  val title: String,
    @field:SerializedName("url")  val url: String,
    @field:SerializedName("price")  val price: Float,
    @field:SerializedName("description")  val description: String
):Serializable