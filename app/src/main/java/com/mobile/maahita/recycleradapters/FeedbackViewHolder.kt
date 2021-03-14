package com.mobile.maahita.recycleradapters

import android.view.View
import android.widget.Button
import android.widget.TextView
import com.mobile.maahita.models.FeedbackRequest
import com.mobile.maahita.models.MaahitaSession
import kotlinx.android.synthetic.main.feedback_session_item.view.*

class FeedbackViewHolder(itemView: View) : BaseViewHolder<FeedbackRequest>(itemView) {

    private var sessionTitle: TextView = itemView.session_title
    private var sessionPresenter: TextView = itemView.session_presenter
    private var feedbackButton: Button = itemView.feedbacksubmit

    override fun bind(item: FeedbackRequest, onDashboardItemClickListener: OnDashboardItemClickListener) {
        sessionTitle.text = item.title
        sessionPresenter.text = item.presenter.displayName

        feedbackButton.setOnClickListener {
            onDashboardItemClickListener.onFeedbackClicked(item)
        }
    }
}