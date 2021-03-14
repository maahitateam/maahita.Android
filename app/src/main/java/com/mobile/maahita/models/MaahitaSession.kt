package com.mobile.maahita.models

import android.graphics.Color
import java.io.Serializable
import java.util.*

enum class SessionStatus(val value: Int) {
    New(1), Enrolled(2), Started(3), Completed(4), Cancelled(5);

    companion object {
        fun fromInt(value: Int) = SessionStatus.values().first { it.value == value }
    }
}

class MaahitaSession(
    var sessionId: String,
    var title: String,
    var description: String,
    var sessiontheme: String,
    var duration: String,
    var status: SessionStatus,
    var presenter: PersonDetails,
    var sessiondate: Date,
    var meetingID: String,
    var isEnrolled: Boolean,
    var noofEnrollments: Int,
    var colorIndicator: Int,
    var amIpresenter: Boolean = false,
    var groupId: String = "",
    var groupName: String = "",
    var isprivate: Boolean = false
) : Serializable {

}

data class PersonDetails(var presenterId: String, var displayName: String) : Serializable {

}