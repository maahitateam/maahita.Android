package com.mobile.maahita.ui.profile_settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.mobile.maahita.repository.FirestoreRepository

class SettingsViewModel : ViewModel() {

    fun changepassword(emailid: String): Task<Void>? {
        return FirebaseAuth.getInstance().sendPasswordResetEmail(emailid)
    }

    fun verifyEmailId(): Task<Void>? {
        return FirebaseAuth.getInstance().currentUser?.sendEmailVerification()
    }

    fun updateFullName(userName: String) {
        FirestoreRepository().updateUserName(userName)
    }

    fun updateAvatar(avatarURL: String) {
        return FirestoreRepository().updateAvatar(avatarURL)
    }

    fun removeAccount(): Task<Void>? {
        return FirebaseAuth.getInstance().currentUser?.delete()
    }

    fun logout() {
        FirestoreRepository().logOut()
    }
}