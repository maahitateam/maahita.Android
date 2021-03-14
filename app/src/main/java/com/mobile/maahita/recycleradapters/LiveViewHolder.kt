package com.mobile.maahita.recycleradapters

import android.view.View
import android.widget.Button
import android.widget.TextView
import com.mobile.maahita.models.LiveSessions
import com.mobile.maahita.models.MaahitaSession
import kotlinx.android.synthetic.main.live_session_item.view.*
import java.text.SimpleDateFormat

class LiveViewHolder(itemView: View) : BaseViewHolder<LiveSessions>(itemView) {

    private var sessionTitle: TextView = itemView.session_title
    private var sessionPresenter: TextView = itemView.session_presenter
    private var sessionTime: TextView = itemView.session_time
    private var livebutton: Button = itemView.attendlive

    override fun bind(item: LiveSessions, onDashboardItemClickListener: OnDashboardItemClickListener) {
        sessionTitle.text = item.title
        sessionPresenter.text = item.presenter.displayName
        var formatter = SimpleDateFormat("hh:mm a")
        sessionTime.text = "Since " + formatter.format(item.sessiondate)

        livebutton.setOnClickListener {
            onDashboardItemClickListener.onLiveClicked(item)
        }
    }
}