package com.vn.thehiveshop.ui.authentication

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vn.thehiveshop.data.repository.AuthenticationRepository

/**
 * Factory class to create instances of [AuthenticationViewModel].
 * @param authenticationRepository Repository to manage authentication-related data.
 * @param context Application context for resource access.
 */
class AuthenticationViewModelFactory(
    private val authenticationRepository: AuthenticationRepository,
    private val context: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthenticationViewModel::class.java) -> {
                AuthenticationViewModel(authenticationRepository, context) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
