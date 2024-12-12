package com.vn.thehiveshop.api

import com.vn.thehiveshop.model.Product
import com.vn.thehiveshop.model.UserModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface HiveService {

    @POST("/authentication/signup")
    suspend fun signUp(@Body user: UserModel)

    @POST("/authentication/signin")
    suspend fun signIn(@Body user: UserModel): UserModel

    @GET("/products?page&per_page")
    suspend fun getAllProducts(
        @Query("page") page: Int,
        @Query("per_page") itemPerPage: Int
    ): List<Product>
    /*@GET("products")
    suspend fun getAllProducts(
        @Query("page") page: Int,
        @Query("per_page") itemPerPage: Int
    ): List<Product>*/


    @PUT("/authentication/profile")
    suspend fun updateProfile(@Body user: UserModel): UserModel


    companion object {
        private const val BASE_URL = "http://10.1.35.183:8080"

        fun create(): HiveService = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(HiveService::class.java)

    }

}