package com.vn.thehiveshop.ui.setting

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.vn.thehiveshop.R
import com.vn.thehiveshop.data.repository.SettingRepository
import com.vn.thehiveshop.model.UserModel
import com.vn.thehiveshop.utils.HandleException
import com.vn.thehiveshop.utils.Resource
import kotlinx.coroutines.Dispatchers

class SettingViewModel(
    private val settingRepository: SettingRepository,
    private val context: Context
) : ViewModel() {

    fun updateUser(user: UserModel) = liveData(Dispatchers.IO) {
        emit(Resource.Loading(null)) // Phát trạng thái đang tải (loading)
        try {
            // Gọi API để cập nhật thông tin người dùng
            val response = settingRepository.updateProfile(user)
            emit(Resource.Success(response)) // Phát trạng thái thành công (success) với dữ liệu trả về
        } catch (ex: Exception) {
            // Xử lý ngoại lệ bằng lớp HandleException để lấy thông báo lỗi
            val errorMessage = HandleException(context).handleException(
                ex,
                R.string.connect_failed,
                R.string.update_profile_failed
            ).message

            emit(Resource.Error(null, errorMessage))
        }
    }

}