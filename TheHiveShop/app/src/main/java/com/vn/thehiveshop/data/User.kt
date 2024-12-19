package com.vn.thehiveshop.data

import com.vn.thehiveshop.model.UserModel

object User {

    var email: String = ""
    var password: String = ""
    var dateOfBirth: String = ""
    var isMale: Boolean = false
    var phoneNumber: String = ""
    var address: String = ""
    var isAdmin: Boolean = false

    fun setNewUser(user: UserModel) {
        this.email = user.email
        this.password = user.password
        this.dateOfBirth = user.dateOfBirth
        this.isMale = user.isMale
        this.phoneNumber = user.phoneNumber
        this.address = user.address
        this.isAdmin = user.isAdmin
    }

    fun getUserInfo() : UserModel {
        return UserModel(email, password, dateOfBirth, isMale, phoneNumber, address, isAdmin)
    }

    fun logOut() {
        this.email = ""
        this.password = ""
        this.dateOfBirth = ""
        this.isMale = true
        this.phoneNumber = ""
        this.address = ""
        this.isAdmin = false
    }

}