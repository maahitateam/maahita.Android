package com.mobile.maahita.ui.meeting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.facebook.react.modules.core.PermissionListener
import com.mobile.maahita.MeetingManager
import com.mobile.maahita.repository.FirestoreRepository
import org.jitsi.meet.sdk.*

class MeetingMaahitaActivity : FragmentActivity(), JitsiMeetActivityInterface {

    private var view: JitsiMeetView? = null

    private var meetingID: String = ""
    private var meetingTitle: String = ""
    private var sessionID: String = ""

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        JitsiMeetActivityDelegate.onActivityResult(
            this, requestCode, resultCode, data
        )
    }

    override fun onBackPressed() {
        JitsiMeetActivityDelegate.onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = JitsiMeetView(this)

        sessionID = intent.getStringExtra("sessionId")
        meetingID = intent.getStringExtra("meetingId")
        meetingTitle = intent.getStringExtra("sessionTitle")

        val meetingManager = MeetingManager()
        val meetingOptions =
            meetingManager.getMeetingOptions(meetingID, meetingTitle)
        view?.join(meetingOptions)
        view?.listener = object : JitsiMeetViewListener {
            override fun onConferenceJoined(map: Map<String, Any>) {
                val firebaseRepository = FirestoreRepository()
                firebaseRepository.attendsession(sessionID)
            }

            override fun onConferenceTerminated(map: Map<String, Any>) {
                JitsiMeetActivityDelegate.onHostDestroy(this@MeetingMaahitaActivity)
            }

            override fun onConferenceWillJoin(map: Map<String, Any>) {
            }
        }

        setContentView(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        view?.dispose()
        view = null
        JitsiMeetActivityDelegate.onHostDestroy(this)
    }

    public override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        JitsiMeetActivityDelegate.onNewIntent(intent)
    }

    override fun requestPermissions(
        permissions: Array<String>,
        requestCode: Int,
        listener: PermissionListener
    ) {
        JitsiMeetActivityDelegate.requestPermissions(this, permissions, requestCode, listener)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        JitsiMeetActivityDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onResume() {
        super.onResume()
        JitsiMeetActivityDelegate.onHostResume(this)
    }

    override fun onStop() {
        super.onStop()
        JitsiMeetActivityDelegate.onHostPause(this)
    }
}