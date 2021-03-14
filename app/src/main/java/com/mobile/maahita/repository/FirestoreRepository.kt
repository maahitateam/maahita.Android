package com.mobile.maahita.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.*
import com.google.firebase.iid.FirebaseInstanceId
import com.mobile.maahita.models.FeedbackData
import com.mobile.maahita.ui.login.LoggedInUserView
import com.mobile.maahita.utilities.Utility
import java.util.*

class FirestoreRepository {

    private var sessionsCollection: String = "sessions"
    private var feedbackCollection: String = "feedback"
    private var usersCollection: String = "users"

    var firestoreDB = FirebaseFirestore.getInstance()
    var firebaseInstance = FirebaseAuth.getInstance()

    init {
        sessionsCollection = "sessions"
        feedbackCollection = "feedback"
        usersCollection = "users"
    }

    fun getsessions(): CollectionReference {
        return firestoreDB.collection(sessionsCollection)
    }

    fun getgroups() : Query? {
        firebaseInstance.currentUser?.uid?.let {
            return firestoreDB.collectionGroup("subscribers").whereEqualTo("id", it)
                .whereEqualTo("isactive", true)
        }
        return null
    }

    fun logOut() {
        return firebaseInstance.signOut()
    }

    fun enrollsession(sessionID: String): Task<Void> {
        val updates =
            hashMapOf<String, Any>("enrollments" to FieldValue.arrayUnion(FirebaseAuth.getInstance().currentUser!!.uid))
        return firestoreDB.collection(sessionsCollection).document(sessionID)
            .set(updates, SetOptions.merge())
    }

    fun attendsession(sessionID: String): Task<Void> {
        val updates =
            hashMapOf<String, Any>("attendees" to FieldValue.arrayUnion(FirebaseAuth.getInstance().currentUser!!.uid))
        return firestoreDB.collection(sessionsCollection).document(sessionID)
            .set(updates, SetOptions.merge())
    }

    fun userDetails(userID: String): Task<DocumentSnapshot> {
        return firestoreDB.collection(usersCollection).document(userID).get()
    }

    fun getAvatar(userID: String): DocumentReference {
        return firestoreDB.collection(usersCollection).document(userID)
    }

    fun getSession(sessionID: String): DocumentReference {
        return firestoreDB.collection(sessionsCollection).document(sessionID)
    }

    fun getSessionsByPresenter(presenterId: String): Query {
        val today = Utility.atStartOfDay()
        return firestoreDB.collection(sessionsCollection)
            .whereGreaterThanOrEqualTo("date", today)
            .whereEqualTo("presenterid", presenterId)
    }

    fun createUser(userName: String, password: String): Task<AuthResult>? {
        val credential = EmailAuthProvider.getCredential(userName, password)
        return firebaseInstance.currentUser?.linkWithCredential(credential)
    }

    fun updateUserName(fullName: String) {
        firebaseInstance.currentUser?.let { currentUser ->
            var profileChangeRequest =
                UserProfileChangeRequest.Builder().setDisplayName(fullName).build()
            currentUser.updateProfile(profileChangeRequest).addOnSuccessListener {
                val updates =
                    hashMapOf<String, Any>("name" to fullName)
                firestoreDB.collection(usersCollection).document(currentUser.uid)
                    .set(updates, SetOptions.merge())
            }
        }
    }

    fun updateAvatar(avatarURL: String) {
        firebaseInstance.currentUser?.let { currentUser ->
            val updates =
                hashMapOf<String, Any>("avatar" to avatarURL)
            firestoreDB.collection(usersCollection).document(currentUser.uid)
                .set(updates, SetOptions.merge())
        }
    }

    fun userAnonymus(): Task<AuthResult> {

        return firebaseInstance.signInAnonymously()
    }

    fun isUserLoggedIn(): Boolean {
        var result = FirebaseAuth.getInstance().currentUser

        if (result != null) {
            return !result.isAnonymous
        }
        return false
    }

    fun getUserDetails(): LoggedInUserView {
        kotlin.run {
            val currentUser = firebaseInstance.currentUser

            return LoggedInUserView(
                userId = currentUser?.uid ?: "",
                displayName = currentUser?.displayName.toString(),
                emailId = currentUser?.email.toString(),
                isLoggedIn = !(currentUser?.isAnonymous ?: false),
                isEmailVerified = currentUser?.isEmailVerified ?: false,
                photoUri = currentUser?.photoUrl
            )
        }
    }

    fun getfeedbackQuestions(): CollectionReference {
        return firestoreDB.collection("feedbackquestions")
    }

    fun getfeedbackRequests(): Query {
        val userID = firebaseInstance.currentUser?.uid ?: ""

        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -5)

        return firestoreDB.collectionGroup(feedbackCollection)
            .whereEqualTo("issubmitted", false)
            .whereGreaterThan("requestedon", cal.time)
            .whereEqualTo("id", userID)
    }

    fun submitFeedback(sessionID: String, feedback: List<FeedbackData>?): Task<Void>? {
        feedback?.let {
            var feedbackdata = feedback.map {
                it.questionID to it.rating
            }.toMap()

            val totalRating = feedback.map { it.rating }.sum()
            val numberofratings: Int = feedback.map { it.rating }.count()

            val overallRating = if (numberofratings > 0) totalRating / numberofratings else 0.0

            val userID = firebaseInstance.currentUser?.uid ?: ""
            firestoreDB.collection("$sessionsCollection/$sessionID/$feedbackCollection")
                .document(userID)
                .update(feedbackdata)

            return firestoreDB.collection("$sessionsCollection/$sessionID/$feedbackCollection")
                .document(userID)
                .update(
                    "overall",
                    overallRating,
                    "issubmitted",
                    true,
                    "submittedon",
                    FieldValue.serverTimestamp()
                )
        }
        return null
    }

    fun generateToken(token : String?) {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            if (!user.isAnonymous) {
                if (token != null) {
                    token?.let {
                        saveTokentoserver(token, user.uid)
                    }
                } else {
                    FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { task ->
                        if (!task.isSuccessful) {
                            return@addOnCompleteListener
                        }
                        val token = task.result?.token ?: ""
                        saveTokentoserver(token, user.uid)
                    }
                }
            }
        }
    }

    private fun saveTokentoserver(token: String, userID: String) {
        val data = hashMapOf<String, Any>("deviceID" to token)
        firestoreDB.collection(usersCollection).document(userID).set(data, SetOptions.merge())
            .addOnSuccessListener {

            }.addOnFailureListener {

            }
    }
}

interface IDataServiceRepository {
    fun getSessions(): CollectionReference
    fun enrollSession(sessionID: String, userID: String): Task<Void>
    fun attendSession(sessionID: String, userID: String): Task<Void>
    fun userDetails(userID: String): Task<DocumentSnapshot>
    fun getSession(sessionID: String): Task<DocumentSnapshot>
    fun getSessionsByPresenter(presenterId: String): Task<QueryDocumentSnapshot>
}