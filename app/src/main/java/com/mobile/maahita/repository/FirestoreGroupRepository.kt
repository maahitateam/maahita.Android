package com.mobile.maahita.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.*
import com.mobile.maahita.models.FeedbackData
import com.mobile.maahita.ui.login.LoggedInUserView
import com.mobile.maahita.utilities.Utility
import java.util.*

class FirestoreGroupRepository {

    private var groupsCollection: String = "maahita-group"
    private var sessionsCollection: String = "group-sessions"
    private var feedbackCollection: String = "feedback"
    private var usersCollection: String = "users"

    var firestoreDB = FirebaseFirestore.getInstance()
    var firebaseInstance = FirebaseAuth.getInstance()

    init {
        sessionsCollection = "group-sessions"
        feedbackCollection = "feedback"
        usersCollection = "users"
    }

    fun getsessions(): Query? {
        FirebaseAuth.getInstance().currentUser?.let {
            val today = Utility.atStartOfDay()
            return firestoreDB.collectionGroup(sessionsCollection)
                .whereArrayContains("subscribers", it.uid)
                .whereGreaterThanOrEqualTo("date", today)
        }
        return null
    }

    fun enrollsession(groupID: String, sessionID: String): Task<Void>? {
        FirebaseAuth.getInstance().currentUser?.uid?.let {
            val updates =
                hashMapOf<String, Any>("enrollments" to FieldValue.arrayUnion(it))
            return firestoreDB.collection("$groupsCollection/$groupID/$sessionsCollection")
                .document(sessionID)
                .set(updates, SetOptions.merge())
        }

        return null
    }

    fun attendsession(groupID: String, sessionID: String): Task<Void>? {
        FirebaseAuth.getInstance().currentUser?.uid?.let {
            val updates =
                hashMapOf<String, Any>("attendees" to FieldValue.arrayUnion(it))
            return firestoreDB.collection("$groupsCollection/$groupID/$sessionsCollection")
                .document(sessionID)
                .set(updates, SetOptions.merge())
        }

        return null
    }

    fun getSession(groupID: String, sessionID: String): DocumentReference {
        return firestoreDB.collection("$groupsCollection/$groupID/$sessionsCollection")
            .document(sessionID)
    }

    fun getSessionsByPresenter(presenterId: String): Query {
        val today = Utility.atStartOfDay()
        return firestoreDB.collection(sessionsCollection)
            .whereGreaterThanOrEqualTo("date", today)
            .whereEqualTo("presenterid", presenterId)
    }

    fun submitFeedback(groupID: String, sessionID: String, feedback: List<FeedbackData>?): Task<Void>? {
        feedback?.let {
            var feedbackdata = feedback.map {
                it.questionID to it.rating
            }.toMap()

            val userID = firebaseInstance.currentUser?.uid ?: ""
            firestoreDB.collection("$groupsCollection/$groupID/$sessionsCollection/$sessionID/$feedbackCollection")
                .document(userID)
                .update(feedbackdata)

            return firestoreDB.collection("$groupsCollection/$groupID/$sessionsCollection/$sessionID/$feedbackCollection")
                .document(userID)
                .update("issubmitted", true, "submittedon", FieldValue.serverTimestamp())
        }
        return null
    }

    private fun atStartOfDay(): Date {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
        return calendar.time
    }
}