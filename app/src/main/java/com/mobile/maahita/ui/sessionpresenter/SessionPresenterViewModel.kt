package com.mobile.maahita.ui.sessionpresenter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobile.maahita.repository.FirestoreRepository
import java.util.ArrayList

class SessionPresenterViewModel : ViewModel() {

    var numberofEnrollments: MutableLiveData<Int> = MutableLiveData()
    var numberofAttendees: MutableLiveData<Int> = MutableLiveData()
    var overallSessionRating: MutableLiveData<Double> = MutableLiveData()

    fun getSessionStats(sessionID: String) {
        FirestoreRepository().getSession(sessionID).addSnapshotListener { snapshot, exception ->
            numberofEnrollments.postValue(
                (snapshot?.get("enrollments") as? ArrayList<*>)?.size ?: 0
            )
            numberofAttendees.postValue((snapshot?.get("attendees") as? ArrayList<*>)?.size ?: 0)
        }

        FirestoreRepository().getSession(sessionID).collection("feedback")?.addSnapshotListener { querySnapshot, firestoreException ->
            querySnapshot?.let {
                var overallRating: Double = 0.0
                var numberofratings: Int = 0
                for (doc in querySnapshot.documents) {
                    if (doc.getBoolean("issubmitted") == true) {
                        overallRating = overallRating.plus(doc.getDouble("overall") ?: 0.0)
                        numberofratings++
                    }
                }

                overallSessionRating.value =
                    if (numberofratings > 0) overallRating / numberofratings else 0.0
            }
        }
    }
}
