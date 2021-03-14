package com.mobile.maahita.recycleradapters

import android.view.View
import android.widget.TextView
import com.mobile.maahita.models.DashboardData
import kotlinx.android.synthetic.main.dashboard_item.view.*

class DashboardViewHolder(itemView: View) : BaseViewHolder<DashboardData>(itemView) {

    private var sessionsEnrolled : TextView = itemView.txtviewSessionsEntrolled
    private var sessionsAttended : TextView = itemView.txtSessionsAttended
    private var groupsSubscription: TextView = itemView.subscriptions

    override fun bind(item: DashboardData, onDashboardItemClickListener: OnDashboardItemClickListener) {
        sessionsEnrolled.text = item.noofEnrollments.toString()
        sessionsAttended.text = item.noofAttended.toString()
        groupsSubscription.text = item.noofGroups.toString()
    }
}