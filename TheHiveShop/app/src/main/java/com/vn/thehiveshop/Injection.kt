package com.vn.thehiveshop

import android.content.Context
import com.vn.thehiveshop.api.HiveService
import com.vn.thehiveshop.data.repository.AuthenticationRepository
import com.vn.thehiveshop.data.repository.ProductRepository
import com.vn.thehiveshop.ui.authentication.AuthenticationViewModelFactory
import com.vn.thehiveshop.ui.product.ProductViewModelFactory

object Injection {
    private fun provideProductRepository() =
        ProductRepository(HiveService.create())

    fun provideProductViewModelFactory() = ProductViewModelFactory(
        provideProductRepository()
    )

    private fun provideAuthenticationRepo() =
        AuthenticationRepository(HiveService.create())

    fun provideAuthenViewModelFactory(context: Context) = AuthenticationViewModelFactory(
        provideAuthenticationRepo(), context
    )
}