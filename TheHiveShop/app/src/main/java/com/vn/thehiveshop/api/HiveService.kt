package com.vn.thehiveshop.api

import com.vn.thehiveshop.model.Product
import com.vn.thehiveshop.model.UserModel
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface HiveService {

    // Đăng ký tài khoản
    @POST("/authentication/signup")
    suspend fun signUp(@Body user: UserModel): Response<Unit> // Trả về trạng thái HTTP qua Response

    // Đăng nhập
    @POST("/authentication/signin")
    suspend fun signIn(@Body user: UserModel): Response<UserModel> // Trả về dữ liệu người dùng

    /*@GET("/products?page&per_page")
    suspend fun getAllProducts(
        @Query("page") page: Int,
        @Query("per_page") itemPerPage: Int
    ): List<Product>*/

    @GET("/products")
    suspend fun getAllProducts(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 10
    ): Response<List<Product>> // Trả về Response để kiểm tra trạng thái HTTP

    // Lấy toàn bộ sản phẩm (nếu cần)
    @GET("/products")
    suspend fun getAllProductsWithoutPaging(): Response<List<Product>>


    // Cập nhật thông tin người dùng
    @PUT("/authentication/profile")
    suspend fun updateProfile(@Body user: UserModel): Response<UserModel>


    companion object {
        private const val BASE_URL = "http://10.1.35.183:8080"

        // Tạo Retrofit instance
        fun create(baseUrl: String = BASE_URL): HiveService {
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(HiveService::class.java)
        }
    }

}