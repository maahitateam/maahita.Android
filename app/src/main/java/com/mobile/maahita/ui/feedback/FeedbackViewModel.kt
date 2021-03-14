package com.mobile.maahita.ui.feedback

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.mobile.maahita.models.FeedbackData
import com.mobile.maahita.repository.FirestoreRepository

class FeedbackViewModel : ViewModel() {

    var firebaseRepository = FirestoreRepository()
    var feedbackQuestions: MutableLiveData<List<FeedbackData>> = MutableLiveData()

    fun getfeedbackQuestions(): LiveData<List<FeedbackData>> {
        firebaseRepository.getfeedbackQuestions().whereEqualTo("isactive", true)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    return@addSnapshotListener
                } else {
                    snapshot?.let {
                        this.feedbackQuestions.value = it.documents?.map { snapshot ->
                            FeedbackData(snapshot.id, snapshot.getString("name") ?: "", 3.0)
                        }
                    }
                }
            }
        return this.feedbackQuestions
    }

    fun submitFeedback(sessionID: String): Task<Void>? {
        return firebaseRepository.submitFeedback(sessionID, feedbackQuestions.value)
    }
}
