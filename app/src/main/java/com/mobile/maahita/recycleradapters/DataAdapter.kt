package com.mobile.maahita.recycleradapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobile.maahita.R
import com.mobile.maahita.models.DashboardData
import com.mobile.maahita.models.FeedbackRequest
import com.mobile.maahita.models.LiveSessions
import com.mobile.maahita.models.MaahitaSession

class DataAdapter(
    var adapterDataList: List<Any>,
    val itemClickListener: OnDashboardItemClickListener
) :RecyclerView.Adapter<BaseViewHolder<*>>() {
    companion object {
        private const val TYPE_LIVE = 0
        private const val TYPE_FEEDBACK = 1
        private const val TYPE_DASHBOARD = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when (viewType) {
            TYPE_LIVE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.live_session_item, parent, false)
                LiveViewHolder(view)
            }
            TYPE_FEEDBACK -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.feedback_session_item, parent, false)
                FeedbackViewHolder(view)
            }
            TYPE_DASHBOARD -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.dashboard_item, parent, false)
                DashboardViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = adapterDataList[position]
        when (holder) {
            is LiveViewHolder -> holder.bind(element as LiveSessions, itemClickListener)
            is FeedbackViewHolder -> holder.bind(element as FeedbackRequest, itemClickListener)
            is DashboardViewHolder -> holder.bind(element as DashboardData, itemClickListener)
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemViewType(position: Int): Int {
        val comparable = adapterDataList[position]
        return when (comparable) {
            is DashboardData -> TYPE_DASHBOARD
            is FeedbackRequest -> TYPE_FEEDBACK
            is LiveSessions -> TYPE_LIVE
            else -> throw IllegalArgumentException("Invalid type of data " + position)
        }
    }

    override fun getItemCount(): Int {
        return adapterDataList.size
    }
}

interface OnDashboardItemClickListener {
    fun onDashboardClicked(dashboardData: DashboardData)
    fun onLiveClicked(liveSession: LiveSessions)
    fun onFeedbackClicked(feedback: FeedbackRequest)
}