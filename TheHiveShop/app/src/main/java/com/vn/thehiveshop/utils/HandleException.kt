package com.vn.thehiveshop.utils

import android.content.Context
import com.vn.thehiveshop.R
import retrofit2.HttpException
import java.io.IOException

class HandleException(
    private val context: Context
) {
    /**
     * Xử lý ngoại lệ và trả về thông báo lỗi tương ứng.
     * @param exception Ngoại lệ xảy ra (IOException hoặc HttpException).
     * @param ioErrorResId Resource ID cho lỗi kết nối mạng.
     * @param httpErrorResId Resource ID cho lỗi HTTP.
     * @return Đối tượng [Resource.Error] chứa thông báo lỗi.
     */
    /*fun handleException(
        exception: Exception,
        ioErrorResId: Int,
        httpErrorResId: Int
    ): Resource.Error<Nothing> {
        return when (exception) {
            is IOException -> Resource.Error(null, context.getString(ioErrorResId))
            is HttpException -> Resource.Error(null, context.getString(httpErrorResId))
            else -> Resource.Error(null, context.getString(R.string.unknown_error))
        }
    }*/

    fun handleException(
        exception: Exception,
        ioErrorResId: Int,
        httpErrorResId: Int
    ): Resource.Error<Nothing> {
        val errorMessage = when (exception) {
            is IOException -> context.getString(ioErrorResId)
            is HttpException -> context.getString(httpErrorResId)
            else -> context.getString(R.string.unknown_error)
        }
        return Resource.Error(null, errorMessage)
    }
}