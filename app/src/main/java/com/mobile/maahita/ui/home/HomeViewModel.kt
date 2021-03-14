package com.mobile.maahita.ui.home

import android.graphics.Color
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.mobile.maahita.models.*
import com.mobile.maahita.repository.FirestoreRepository
import com.mobile.maahita.ui.login.LoggedInUserView
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList
import kotlin.reflect.typeOf

open class SessionsListViewModel : ViewModel() {

    var firebaseRepository = FirestoreRepository()
    var userDashboardData: MutableLiveData<List<Any>> = MutableLiveData()
    var listItems = ArrayList<Any>()
    var dashboardData: DashboardData? = null

    fun attendLive(sessionID: String): Task<Void>? {
        return firebaseRepository.attendsession(sessionID)
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

class HomeViewModel : SessionsListViewModel() {

    var loggedInUserView: MutableLiveData<LoggedInUserView> = MutableLiveData()

    fun setUserInfo() {
        if (firebaseRepository.isUserLoggedIn()) {
            loggedInUserView.value = firebaseRepository.getUserDetails()
        } else {
            SetDefaultUser()
        }
    }

    fun IsUserLoggedIn(): Boolean? {
        return firebaseRepository.isUserLoggedIn()
    }

    fun SetDefaultUser() {
        firebaseRepository.userAnonymus().addOnSuccessListener {
            loggedInUserView.value = LoggedInUserView(
                "",
                "māhita guest",
                "guest email",
                false,
                true,
                null
            )

            getdashboardData()
        }
    }

    fun getdashboardData() {
        if (IsUserLoggedIn() == true) {
            firebaseRepository.getsessions().addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    return@addSnapshotListener
                } else {
                    listItems = ArrayList<Any>()
                    snapshot?.let { querySnapshot ->
                        val ongoingSessions = querySnapshot.documents.filter { documentSnapshot ->
                            val sessionStatus =
                                SessionStatus.fromInt(
                                    (documentSnapshot.getLong("status") ?: 1).toInt()
                                )
                            sessionStatus === SessionStatus.Started
                        }

                        val liveSessions = ongoingSessions.map { doc ->
                            LiveSessions(
                                doc.id,
                                doc.getString("title") ?: "",
                                PersonDetails("", doc.getString("presenter") ?: ""),
                                (doc.getDate("date") ?: Date()),
                                doc.getString("meetingID") ?: ""
                            )
                        }

                        listItems.addAll(0, liveSessions)
                        userDashboardData.postValue(listItems)

                        var userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

                        val enrolled = querySnapshot.documents.filter { documentSnapshot ->
                            val enrollments = documentSnapshot.get("enrollments") as ArrayList<*>?
                            enrollments?.contains(userId) ?: false
                        }
                        var eCount = enrolled.count()

                        val attended = querySnapshot.documents.filter { documentSnapshot ->
                            val enrollments = documentSnapshot.get("attendees") as ArrayList<*>?
                            enrollments?.contains(userId) ?: false
                        }
                        var aCount = attended.count()

                        dashboardData =
                            DashboardData(eCount, aCount, dashboardData?.noofGroups ?: 0)
                        val index =
                            listItems.indexOf(listItems.find { item -> item is DashboardData })
                        if (index < 0) {
                            listItems.add(dashboardData!!)
                        } else {
                            listItems[index] = dashboardData!!
                        }
                        userDashboardData.postValue(listItems)

                        getFeedbackRequests()
                    }

                    getSubscribedGroups()

                }
            }
        } else {
            listItems = ArrayList<Any>()
            dashboardData = DashboardData(0, 0, 0)
            listItems.add(dashboardData!!)
            userDashboardData.postValue(listItems)
        }
    }

    fun getSubscribedGroups() {
        firebaseRepository.getgroups()?.addSnapshotListener { snapshot, exception ->
            snapshot?.let { querySnapshot ->

                dashboardData = DashboardData(
                    dashboardData?.noofEnrollments ?: 0,
                    dashboardData?.noofAttended ?: 0,
                    querySnapshot.documents.size
                )
                val index = listItems.indexOf(listItems.find { item -> item is DashboardData })
                if (index < 0) {
                    listItems.add(dashboardData!!)
                } else {
                    listItems[index] = dashboardData!!
                }
                userDashboardData.postValue(listItems)
            }
        }
    }

    fun getFeedbackRequests() {
        firebaseRepository.getfeedbackRequests().get().addOnSuccessListener { snapshot ->
            snapshot?.documents?.forEach { sessiondoc ->
                sessiondoc.reference.parent.parent?.get()?.addOnSuccessListener {
                    val feedbackRequest = FeedbackRequest(
                        it.id, it.getString("title") ?: "",
                        PersonDetails(
                            it.getString("presenterid") ?: "",
                            it.getString("presenter") ?: ""
                        )
                    )

                    val foundItem: Any? =
                        listItems.find { item -> item is FeedbackRequest && item.sessionId == feedbackRequest.sessionId }

                    if (foundItem == null) {
                        val dashboardIndex =
                            listItems.indexOf(listItems.find { item -> item is DashboardData })
                        if (dashboardIndex < 0) {
                            listItems.add(0, feedbackRequest)
                        } else {
                            listItems.add(dashboardIndex, feedbackRequest)
                        }
                        userDashboardData.postValue(listItems)
                    }
                }
            }
        }
    }
}