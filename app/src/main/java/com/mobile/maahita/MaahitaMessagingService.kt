package com.mobile.maahita

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mobile.maahita.repository.FirestoreRepository

class MaahitaMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

    }

    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)

        FirestoreRepository().generateToken(newToken)
    }
}