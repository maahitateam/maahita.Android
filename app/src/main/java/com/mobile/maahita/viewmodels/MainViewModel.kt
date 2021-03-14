package com.mobile.maahita.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import com.mobile.maahita.models.MaahitaSession
import com.mobile.maahita.models.SessionStatus
import com.mobile.maahita.repository.FirestoreRepository

class MainViewModel : ViewModel() {
    var firebaseRepository = FirestoreRepository()
    var maahitaSessions : MutableLiveData<List<MaahitaSession>> = MutableLiveData()
}