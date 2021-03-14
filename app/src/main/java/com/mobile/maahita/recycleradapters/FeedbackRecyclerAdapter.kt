package com.mobile.maahita.recycleradapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobile.maahita.R
import com.mobile.maahita.models.FeedbackData
import kotlinx.android.synthetic.main.feedback_list_item.view.*

class SessionFeedbackViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var feedbackQuestion: TextView = itemView.feedbackQuestion
    private var feedbackSeeker: RatingBar = itemView.feedbacknum

    fun bind(feedback: FeedbackData, seekListener: OnItemSeekListener) {
        feedbackQuestion.text = feedback.question
        feedbackSeeker.setOnRatingBarChangeListener { bar, rating, fromUser ->
            feedback.rating = rating.toDouble()
            seekListener.onFeedbackSeeked(feedback)
        }
    }
}

class FeedbackRecyclerAdapter(
    var items: List<FeedbackData>,
    val itemSeekListener: OnItemSeekListener
) : RecyclerView.Adapter<SessionFeedbackViewHolder>() {
    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionFeedbackViewHolder {
        return SessionFeedbackViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.feedback_list_item,
                    parent,
                    false
                )
        )
    }

    override fun onBindViewHolder(holder: SessionFeedbackViewHolder, position: Int) {
        when (holder) {
            is SessionFeedbackViewHolder -> {
                holder.bind(items[position], itemSeekListener)
            }
        }
    }
}

interface OnItemSeekListener {
    fun onFeedbackSeeked(feedback: FeedbackData)
}