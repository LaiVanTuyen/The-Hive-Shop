package com.vn.thehiveshop.data.repository

import com.vn.thehiveshop.api.HiveService
import com.vn.thehiveshop.model.UserModel

class SettingRepository(private val hiveService: HiveService) {
    suspend fun updateProfile(user: UserModel) = hiveService.updateProfile(user)
}