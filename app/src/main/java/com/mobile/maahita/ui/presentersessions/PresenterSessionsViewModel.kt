package com.mobile.maahita.ui.presentersessions

import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.mobile.maahita.models.MaahitaSession
import com.mobile.maahita.models.PersonDetails
import com.mobile.maahita.models.SessionStatus
import com.mobile.maahita.repository.FirestoreRepository
import java.util.*
import kotlin.collections.ArrayList

class PresenterSessionsViewModel : ViewModel() {

    var firebaseRepository = FirestoreRepository()
    var presenterSessions: MutableLiveData<List<MaahitaSession>> = MutableLiveData()

    fun getSessionsByPresenter(presenterId: String, excludeSession: String) {
        firebaseRepository.getSessionsByPresenter(presenterId)
            .addSnapshotListener { querysnapshot, exception ->
                val sessions = querysnapshot?.documents?.map { snapshot ->
                    val isEnrolled: Boolean =
                        (snapshot.get("enrollments") as? ArrayList<String>)?.contains(
                            FirebaseAuth.getInstance().currentUser?.uid ?: ""
                        ) ?: false

                    MaahitaSession(
                        snapshot.id.toString(),
                        snapshot.getString("title") ?: "",
                        snapshot.getString("description") ?: "",
                        snapshot.getString("theme") ?: "",
                        snapshot.getString("duration") ?: "",
                        SessionStatus.fromInt((snapshot.getLong("status") ?: 1).toInt()),
                        PersonDetails(
                            "", snapshot.getString("presenter") ?: ""
                        ),
                        (snapshot.getDate("date") ?: Date()),
                        snapshot.getString("meetingID") ?: "",
                        isEnrolled,
                        (snapshot.get("enrollments") as? ArrayList<*>)?.count() ?: 0,
                        getColor(
                            SessionStatus.fromInt(
                                (snapshot.getLong("status") ?: 1).toInt()
                            ), isEnrolled
                        )
                    )
                }

                sessions?.let {
                    val filtered =
                        sessions.filter { sess -> sess.sessionId != excludeSession }
                    presenterSessions.value = filtered
                }
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
