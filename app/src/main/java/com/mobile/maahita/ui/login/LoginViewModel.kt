package com.mobile.maahita.ui.login

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.mobile.maahita.repository.FirestoreRepository
import java.io.Serializable

class LoginViewModel : ViewModel() {

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult
    private var firebaseRepository = FirestoreRepository()

    init {
        _loginResult.value = LoginResult()
    }

    fun login(userName: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(userName, password)
            .addOnSuccessListener {
                _loginResult.value = LoginResult(success = firebaseRepository.getUserDetails())
                firebaseRepository.generateToken(null)
            }
            .addOnFailureListener {
                _loginResult.value = LoginResult(error = it.localizedMessage)
            }
    }
}

data class LoginResult(
    val success: LoggedInUserView? = null,
    val error: String? = null
)

data class LoggedInUserView(
    var userId: String,
    val displayName: String,
    val emailId: String,
    val isLoggedIn: Boolean,
    val isEmailVerified: Boolean,
    val photoUri: Uri?
) : Serializable