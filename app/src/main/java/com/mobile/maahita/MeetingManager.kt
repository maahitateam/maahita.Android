package com.mobile.maahita

import com.google.firebase.auth.FirebaseAuth
import org.jitsi.meet.sdk.JitsiMeet
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import org.jitsi.meet.sdk.JitsiMeetUserInfo
import java.net.URL

class MeetingManager {

    fun getMeetingOptions(meetingID: String, title: String): JitsiMeetConferenceOptions? {
        var userInfo = JitsiMeetUserInfo()
        userInfo.displayName = "māhita mobile"

        FirebaseAuth.getInstance().currentUser?.let { user ->
            if (!user.displayName.isNullOrEmpty()) {
                userInfo.displayName = user.displayName
            }

            user.photoUrl?.let { uri ->
                userInfo.avatar = URL(uri.toString())
            }
        }

        val serverURL = URL("https://meetings.maahita.com")

        val options = JitsiMeetConferenceOptions.Builder()
            .setServerURL(serverURL)
            .setAudioMuted(true)
            .setVideoMuted(true)
            .setRoom(meetingID)
            .setUserInfo(userInfo)
            .setSubject(title)
            .setFeatureFlag("live-streaming.enabled", false)
            .setFeatureFlag("recording.enabled", false)
            .setFeatureFlag("invite.enabled", false)
            .setFeatureFlag("pip.enabled", false)
            .setWelcomePageEnabled(false)
            .build()
        return options
    }
}