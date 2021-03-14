package com.mobile.maahita.ui.signup

import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobile.maahita.repository.FirestoreRepository
import java.util.*
import java.util.regex.Pattern

class SignUpViewModel : ViewModel() {


    var fullname = ObservableField<String>("")
    var emailid = ObservableField<String>("")
    var password = ObservableField<String>("")
    var confirmPassword = ObservableField<String>("")

    var resultData = MutableLiveData<Boolean>()
    var firebaseRepository: FirestoreRepository = FirestoreRepository()

    var passwordError = MutableLiveData<String>()
    var confPasswordError = MutableLiveData<String>()
    var emailError = MutableLiveData<String>()

    var signupStatus = MutableLiveData<Boolean>()
    private var isLoading = MutableLiveData<Boolean>()

    fun register() {

        if (fragmentValidation()) {
            firebaseRepository.createUser(emailid.get().toString(), password.get().toString())
                ?.addOnSuccessListener {
                    firebaseRepository.updateUserName(fullname.get().toString())
                    firebaseRepository.generateToken(null)
                    resultData.value = true
                }?.addOnFailureListener {
                    resultData.value = false
                }
        }
    }

    fun SetSignInStatus(status:Boolean){
        signupStatus.postValue(status)
    }

    fun fragmentValidation(): Boolean {

        if (fullname?.get()?.isEmpty()!!) {
            return false
        }

        if (emailid.get()?.isBlank()!!) {
            emailError.postValue("User email is required")
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailid.get()).matches()) {
            emailError.postValue("Invalid email id")
            return false
        }

        if (password?.get().isNullOrEmpty()) {
            passwordError.postValue("Password is required")
            return false
        }
        if (password?.get()?.length!! < 8) {
            passwordError.postValue("Password length must be grather than 8 charecters")
            return false
        }

        if (password.get() != confirmPassword.get()) {
            confPasswordError.postValue("confirm password must match with password")
            return false
        }
        return true
    }
}

