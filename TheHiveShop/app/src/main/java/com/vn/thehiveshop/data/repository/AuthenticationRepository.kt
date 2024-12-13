package com.vn.thehiveshop.data.repository

import com.vn.thehiveshop.api.HiveService
import com.vn.thehiveshop.model.UserModel

class AuthenticationRepository(private val service: HiveService) {

    suspend fun signUp(user: UserModel) = service.signUp(user)

    suspend fun signIn(user: UserModel) = service.signIn(user)

}