package com.mobile.maahita.ui.privatesessions

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.mobile.maahita.models.MaahitaSession
import com.mobile.maahita.models.PersonDetails
import com.mobile.maahita.models.SessionStatus
import com.mobile.maahita.repository.FirestoreGroupRepository
import com.mobile.maahita.repository.FirestoreRepository
import java.util.*
import kotlin.collections.ArrayList

class PrivateSessionsViewModel : ViewModel() {
    var firebaseRepository = FirestoreGroupRepository()
    var maahitaSessions: MutableLiveData<List<MaahitaSession>> = MutableLiveData()

    var isLoading: MutableLiveData<Boolean> = MutableLiveData()

    fun IsLoading(): Boolean {
        return isLoading.value ?: false
    }

    public fun getSessions() {
        val userID : String = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        firebaseRepository.getsessions()?.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                isLoading.value = false
                return@addSnapshotListener
            } else {
                snapshot?.let {
                    val docs = it.documents
                    isLoading.value = false
                    this.maahitaSessions.value = docs?.map { snapshot ->
                        val isEnrolled: Boolean =
                            (snapshot.get("enrollments") as? ArrayList<*>)?.contains(
                                FirebaseAuth.getInstance().currentUser?.uid ?: ""
                            ) ?: false
                        val sessionStatus =
                            SessionStatus.fromInt(
                                (snapshot.getLong("status") ?: 1).toInt()
                            )
                        MaahitaSession(
                            snapshot.id.toString(),
                            snapshot.getString("title") ?: "",
                            snapshot.getString("description") ?: "",
                            snapshot.getString("subject") ?: "",
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
                            getColor(sessionStatus, isEnrolled),
                            false,
                            snapshot.getString("groupid") ?: "",
                            snapshot.getString("groupname") ?: "",
                            true
                        )
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

    private fun getColor(sessionStatus: SessionStatus, isEnrolled: Boolean): Int {

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
