package com.companion.android

import android.util.Log
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR

abstract class AuthenticationViewModel: BaseObservable() {
    val user = User()

    @Bindable
    fun getEmail(): String {
        return user.email
    }

    fun setEmail(value: String) {
        // Avoids infinite loops.
        if (user.email != value) {
            user.email = value

            // Notify observers of a new value.
            notifyPropertyChanged(BR.email)
        }
    }

    @Bindable
    fun getName(): String {
        return user.name
    }

    fun setName(value: String) {
        // Avoids infinite loops.
        if (user.name != value) {
            user.name = value

            // Notify observers of a new value.
            notifyPropertyChanged(BR.model)
        }
    }

    @Bindable
    fun getPassword(): String? {
        return user.password
    }

    abstract fun setPassword(value: String)

    fun removePassword() {
        user.removePassword()
    }
}