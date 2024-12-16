package com.vn.thehiveshop.ui.setting

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vn.thehiveshop.data.repository.SettingRepository
import com.vn.thehiveshop.ui.product.ProductViewModel

class SettingViewModelFactory(
    private val settingRepository: SettingRepository,
    private val context: Context
) :
    ViewModelProvider.Factory {
    // Factory cho ViewModel, cho phép khởi tạo với ProductRepository
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            return SettingViewModel(settingRepository, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}