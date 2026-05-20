package com.katarina.task4.util

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import com.katarina.task4.R

class FirebaseHelper {
    companion object {
        fun getDatabase() = FirebaseDatabase.getInstance().reference

        fun getAuth() = FirebaseAuth.getInstance()

        fun getIdUser() = getAuth().currentUser?.uid ?: ""

        fun isAutenticated() = getAuth().currentUser != null

        fun validError(error: String): Int {
            return when {
                error.contains(other = "INVALID_LOGIN_CREDENTIALS") -> {
                    R.string.account_not_registered_register_fragment
                }

                error.contains(other = "The supplied auth credential is incorrect, malformed or has expired") -> {
                    R.string.account_not_registered_register_fragment
                }

                error.contains(other = "There is no user record corresponding to this identifier") -> {
                    R.string.account_not_registered_register_fragment
                }

                error.contains(other = "The email address is badly formatted") -> {
                    R.string.invalid_email_register_fragment
                }

                error.contains(other = "The password is invalid or the user does not have a password") -> {
                    R.string.invalid_password_register_fragment
                }

                error.contains(other = "The email address is already in use by another account") -> {
                    R.string.email_in_user_register_fragment
                }

                error.contains(other = "Password should be at least 6 characters") -> {
                    R.string.strong_password_register_fragment
                }

                else -> {
                    R.string.error_generic
                }
            }
        }
    }
}