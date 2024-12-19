package com.vn.thehiveshop.ui.authentication

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.vn.thehiveshop.R
import com.vn.thehiveshop.data.repository.AuthenticationRepository
import com.vn.thehiveshop.model.UserModel
import com.vn.thehiveshop.utils.Resource
import kotlinx.coroutines.Dispatchers
import retrofit2.HttpException
import java.io.IOException

class AuthenticationViewModel(
    private val authenticationRepository: AuthenticationRepository,
    private val context: Context
) : ViewModel() {

    /**
     * Đăng ký tài khoản mới.
     * @param user Đối tượng [UserModel] chứa thông tin người dùng.
     */
    fun signUp(user: UserModel) = liveData(Dispatchers.IO) {
        emit(Resource.Loading(null)) // Trạng thái đang tải
        try {
            val response = authenticationRepository.signUp(user)
            emit(Resource.Success(response)) // Thành công
        } catch (exception: Exception) {
            emit(handleException(exception, R.string.connect_failed, R.string.coincide_email))
        }
    }

    /**
     * Đăng nhập.
     * @param user Đối tượng [UserModel] chứa thông tin đăng nhập.
     */
    fun signIn(user: UserModel) = liveData(Dispatchers.IO) {
        emit(Resource.Loading(null)) // Trạng thái đang tải
        try {
            val response = authenticationRepository.signIn(user)
            emit(Resource.Success(response)) // Thành công
        } catch (exception: Exception) {
            emit(handleException(exception, R.string.connect_failed, R.string.login_failed))
        }
    }

    /**
     * Xử lý ngoại lệ và trả về thông báo lỗi tương ứng.
     * @param exception Ngoại lệ xảy ra (IOException hoặc HttpException).
     * @param ioErrorResId Resource ID cho lỗi kết nối mạng.
     * @param httpErrorResId Resource ID cho lỗi HTTP.
     * @return Đối tượng [Resource.Error] chứa thông báo lỗi.
     */
    private fun handleException(exception: Exception, ioErrorResId: Int, httpErrorResId: Int): Resource.Error<Nothing> {
        return when (exception) {
            is IOException -> Resource.Error(null, context.getString(ioErrorResId))
            is HttpException -> Resource.Error(null, context.getString(httpErrorResId))
            else -> Resource.Error(null, context.getString(R.string.unknown_error))
        }
    }
}
