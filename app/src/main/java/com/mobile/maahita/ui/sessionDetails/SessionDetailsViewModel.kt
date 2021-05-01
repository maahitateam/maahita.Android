package com.mobile.maahita.ui.sessionDetails

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.mobile.maahita.MeetingManager
import com.mobile.maahita.models.MaahitaSession
import com.mobile.maahita.models.PersonDetails
import com.mobile.maahita.models.SessionStatus
import com.mobile.maahita.repository.FirestoreGroupRepository
import com.mobile.maahita.ui.home.SessionsListViewModel
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

data class ButtonBehaviour(
    val displayText: String,
    val isEnabled: Boolean,
    val showButton: Boolean
) {

}

enum class EnrollStatusCode(val status: Int) {
    Success(200),
    Fail(201),
    Exist(202)
}

class SessionDetailsViewModel : SessionsListViewModel() {

    var session: MutableLiveData<MaahitaSession> = MutableLiveData()
    var presenterSessions: MutableLiveData<List<MaahitaSession>> = MutableLiveData()
    var enrolledButtonBehaviour: MutableLiveData<ButtonBehaviour> = MutableLiveData()
    var sharebuttonBehaviour: MutableLiveData<ButtonBehaviour> = MutableLiveData()

    var isLoading: MutableLiveData<Boolean> = MutableLiveData()
    var enrollStatusCode: MutableLiveData<EnrollStatusCode> = MutableLiveData()
    var presenterAvatar: MutableLiveData<String> = MutableLiveData()

    private val sessionID: String?
        get() {
            return session?.value?.sessionId
        }

    fun isUserLogin(): Boolean {
        return firebaseRepository.isUserLoggedIn()!!
    }

    fun enroll() {
        session?.value?.let {
            if (it.isprivate) {
                FirestoreGroupRepository().enrollsession(it.groupId, it.sessionId)
                    ?.addOnFailureListener {
                        enrollStatusCode.value = EnrollStatusCode.Fail
                    }?.addOnSuccessListener {
                        enrollStatusCode.value = EnrollStatusCode.Success
                        updatebuttons()
                    }
            } else {
                firebaseRepository.enrollsession(it.sessionId)
                    .addOnFailureListener {
                        enrollStatusCode.value = EnrollStatusCode.Fail
                    }
                    .addOnSuccessListener {
                        enrollStatusCode.value = EnrollStatusCode.Success
                        updatebuttons()
                    }
            }
        }
    }

    fun attend(): Task<Void>? {
        session?.value?.let {
            if (it.isprivate) {
                return FirestoreGroupRepository().attendsession(it.groupId, it.sessionId)
            } else {
                return firebaseRepository.attendsession(it.sessionId)
            }
        }

        return null
    }

    fun IsReadyToLaunch(): Boolean? {
        return (session?.value?.isEnrolled
            ?: false) || session?.value?.status == SessionStatus.Started
    }

    fun getSessionDate(): String {
        var formatter = SimpleDateFormat("dd-MMM hh:mm a z")
        return formatter.format(session.value?.sessiondate)
    }

    fun getSessionTime(): String {
        var formatter = SimpleDateFormat("hh:mm a")
        return formatter.format(session.value?.sessiondate)
    }

    fun getMeetingOptions(context: Context): JitsiMeetConferenceOptions? {
        val meetingManager = MeetingManager(context)
        val meetingOptions = meetingManager.getMeetingOptions(
            session.value?.meetingID ?: "",
            session.value?.title ?: ""
        )
        return meetingOptions
    }

    fun getPresenterAvatar(userID: String) {
        firebaseRepository.getAvatar(userID).addSnapshotListener { snapshot, exception ->
            presenterAvatar.value = snapshot?.getString("avatar") ?: ""
        }
    }

    fun getSession(sessionID: String, isPrivateSession: Boolean = false, groupID: String = "") {
        isLoading.value = true

        val sessionReference =
            if (isPrivateSession) FirestoreGroupRepository().getSession(groupID, sessionID)
            else firebaseRepository.getSession(sessionID)

        sessionReference.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                isLoading.value = false
            } else {
                snapshot?.let {
                    val userID: String = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                    val isEnrolled: Boolean =
                        (it.get("enrollments") as? ArrayList<*>)?.contains(userID) ?: false
                    val amIpresenter: Boolean = it.getString("presenterid") ?: "" == userID

                    session.value = MaahitaSession(
                        it.id.toString(),
                        it.getString("title") ?: "",
                        it.getString("description") ?: "",
                        it.getString("theme") ?: "",
                        it.getString("duration") ?: "",
                        SessionStatus.fromInt((it.getLong("status") ?: 1).toInt()),
                        PersonDetails(
                            it.getString("presenterid") ?: "", it.getString("presenter") ?: ""
                        ),
                        (it.getDate("date") ?: Date()),
                        it.getString("meetingID") ?: "",
                        isEnrolled,
                        (it.get("enrollments") as? ArrayList<*>)?.count() ?: 0,
                        getColor(
                            SessionStatus.fromInt(
                                (it.getLong("status") ?: 1).toInt()
                            ), isEnrolled
                        ),
                        amIpresenter,
                        snapshot.getString("groupid") ?: "",
                        snapshot.getString("groupname") ?: "",
                        isPrivateSession
                    )

                    updatebuttons()

                    getSessionsByPresenter(it.getString("presenterid") ?: "")

                    it.getString("presenterid")?.let { prenseterid ->
                        getPresenterAvatar(prenseterid)
                    }

                    isLoading.value = false
                }
            }
        }
    }

    private fun updatebuttons() {
        isLoading.value = true
        //TODO: Code optimization
        val sessionModel = session?.value

        sessionModel?.let {
            enrolledButtonBehaviour.value = when (sessionModel.status) {
                SessionStatus.Cancelled -> ButtonBehaviour("Cancelled", false, true)
                SessionStatus.Completed -> ButtonBehaviour("Completed", false, true)
                SessionStatus.Started -> ButtonBehaviour("Attend", true, true)
                else -> if (sessionModel.isEnrolled) {
                    ButtonBehaviour(
                        "Attend",
                        false,
                        true
                    )
                } else {
                    var userDetails = firebaseRepository.getUserDetails()
                    if (sessionModel.presenter.presenterId == userDetails.userId) {
                        ButtonBehaviour(
                            "Attend",
                            false,
                            true
                        )
                    } else {
                        ButtonBehaviour("Enroll", true, !sessionModel.isprivate)
                    }
                }
            }

            sharebuttonBehaviour.value = when (sessionModel.status) {
                SessionStatus.New, SessionStatus.Enrolled, SessionStatus.Started -> ButtonBehaviour(
                    "Share",
                    true,
                    !sessionModel.isprivate
                )
                else -> ButtonBehaviour("Share", false, false)
            }
        }
        isLoading.value = false
    }

    private fun getSessionsByPresenter(presenterId: String) {
        firebaseRepository.getSessionsByPresenter(presenterId)
            .addSnapshotListener { querysnapshot, exception ->
                isLoading.value = false

                val sessions = querysnapshot?.documents?.map { snapshot ->
                    val isEnrolled: Boolean =
                        (snapshot.get("enrollments") as? ArrayList<*>)?.contains(
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
                        sessions.filter { sess -> sess.sessionId != session.value?.sessionId }
                    presenterSessions.value = filtered
                }
            }
    }
}