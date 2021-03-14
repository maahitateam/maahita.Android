package com.mobile.maahita

import android.app.NotificationChannel
import android.app.NotificationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.mobile.maahita.repository.FirestoreRepository
import com.mobile.maahita.ui.login.LoggedInUserView


class MainActivity : AppCompatActivity() {

    private lateinit var userView: LoggedInUserView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide() //Hide Application bar

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "MaahitaNotifications",
                "MaahitaNotifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        FirebaseMessaging.getInstance().subscribeToTopic("maahitaNotifications")
            .addOnCompleteListener {
                OnCompleteListener<Task<Void>> {
                }
            }

        Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData ->
                // Get deep link from result (may be null if no link is found)
                var deepLink: Uri? = null
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link
                }

                if (deepLink != null) {
                    deepLink.getQueryParameter("sess")?.let { sessionID ->
                        FirestoreRepository().getSession(sessionID).addSnapshotListener { snapshot, exception ->

                        }
                    }
                } else {
                    Log.d("DeepLink", "getDynamicLink: no link found")
                }
            }
            .addOnFailureListener(this) { e ->
                Log.w("DeepLink", "getDynamicLink:onFailure", e)
            }
    }
}
