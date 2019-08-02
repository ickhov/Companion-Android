package com.companion.android

data class User (var _email: String = "", var _name: String = "", var _password: String = "") {
    var email: String = _email
    var name: String = _name
    var password: String? = _password

    fun removePassword() {
        password = null
    }
}