package com.mobile.maahita.ui.opensessions

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.mobile.maahita.models.MaahitaSession
import com.mobile.maahita.models.PersonDetails
import com.mobile.maahita.models.SessionStatus
import com.mobile.maahita.repository.FirestoreRepository
import com.mobile.maahita.utilities.Utility
import java.util.*
import kotlin.collections.ArrayList

class OpenSessionsViewModel : ViewModel() {
    var firebaseRepository = FirestoreRepository()
    var maahitaSessions: MutableLiveData<List<MaahitaSession>> = MutableLiveData()

    var isLoading: MutableLiveData<Boolean> = MutableLiveData()

    fun IsLoading(): Boolean {
        return isLoading.value ?: false
    }

    public fun getSessions() {

        val today = Utility.atStartOfDay()
        val userID : String = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        firebaseRepository.getsessions().whereGreaterThanOrEqualTo("date", today)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    return@addSnapshotListener
                } else {
                    snapshot?.let {
                        val docs = it.documents

                        this.maahitaSessions.value = docs?.map { snapshot ->
                            kotlin.run {
                                val isEnrolled: Boolean = (snapshot.get("enrollments") as? ArrayList<*>)?.contains(userID) ?: false
                                val sessionStatus =
                                    SessionStatus.fromInt(
                                        (snapshot.getLong("status") ?: 1).toInt()
                                    )
                                MaahitaSession(
                                    snapshot.id.toString(),
                                    snapshot.getString("title") ?: "",
                                    snapshot.getString("description") ?: "",
                                    snapshot.getString("theme") ?: "",
                                    snapshot.getString("duration") ?: "",
                                    getStatus(sessionStatus, isEnrolled),
                                    PersonDetails(
                                        snapshot.getString("presenterid") ?: "", snapshot.getString("presenter") ?: ""
                                    ),
                                    (snapshot.getDate("date") ?: Date()),
                                    snapshot.getString("meetingID") ?: "",
                                    isEnrolled,
                                    (snapshot.get("enrollments") as? ArrayList<*>)?.count()
                                        ?: 0,
                                    getColor(sessionStatus, isEnrolled)
                                )
                            }
                        }
                    }
                }
            }
    }

    fun getStatus(sessionStatus: SessionStatus, isEnrolled: Boolean): SessionStatus {
        return when (sessionStatus) {
            SessionStatus.New -> {
                if (isEnrolled) {
                    SessionStatus.Enrolled
                } else {
                    SessionStatus.New
                }
            }
            else -> sessionStatus
        }
    }

    protected fun getColor(sessionStatus: SessionStatus, isEnrolled: Boolean): Int {

        return when (sessionStatus) {
            SessionStatus.New -> kotlin.run {
                if (isEnrolled) Color.parseColor("#65aa49") else Color.parseColor(
                    "#f42c2f"
                )
            }
            SessionStatus.Enrolled -> Color.parseColor("#65aa49")
            SessionStatus.Started -> Color.parseColor("#8135b7")
            SessionStatus.Completed -> Color.parseColor("#103eb5")
            SessionStatus.Cancelled -> Color.parseColor("#63625e")
        }
    }
}
