package com.vn.thehiveshop.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserModel(
    @field:SerializedName("email") var email: String,
    @field:SerializedName("password") var password: String,
    @field:SerializedName("date_of_birth") var dateOfBirth: String,
    @field:SerializedName("is_male") var isMale: Boolean,
    @field:SerializedName("phone_number") var phoneNumber: String,
    @field:SerializedName("address") var address: String,
    @field:SerializedName("is_admin") var isAdmin: Boolean
) : Serializable {
    override fun toString(): String {
        return "User(email='$email', password='$password', dateOfBirth='$dateOfBirth', isMale=$isMale, phoneNumber='$phoneNumber', address='$address', isAdmin=$isAdmin)"
    }
}