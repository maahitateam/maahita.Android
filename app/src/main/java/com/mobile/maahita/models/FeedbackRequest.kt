package com.mobile.maahita.models

import java.io.Serializable

class FeedbackRequest(
    var sessionId: String,
    var title: String,
    var presenter: PersonDetails
) : Serializable {

}