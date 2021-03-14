package com.mobile.maahita.recycleradapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobile.maahita.R
import com.mobile.maahita.models.MaahitaSession
import kotlinx.android.synthetic.main.fragment_session_details.view.*
import kotlinx.android.synthetic.main.layout_session_list_item.view.*
import kotlinx.android.synthetic.main.layout_session_list_item.view.session_description
import kotlinx.android.synthetic.main.layout_session_list_item.view.session_presenter
import kotlinx.android.synthetic.main.layout_session_list_item.view.session_title
import java.text.SimpleDateFormat

class SessionViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var sessionTitle: TextView = itemView.session_title
    private var sessionDesc: TextView = itemView.session_description
    private var sessionPresenter: TextView = itemView.session_presenter
    private var sessionDay: TextView = itemView.session_day
    private var sessionMonth: TextView = itemView.session_month
    private var sessionTime: TextView = itemView.session_time
    private var sessionStatus: TextView = itemView.sessionStatus
    private var sessionIndicator: TextView = itemView.sessionStatusIndicator
    private var themeLabel: TextView = itemView.themename

    fun bind(maahitaSession: MaahitaSession, clickListener: OnItemClickListener) {
        sessionTitle.text = maahitaSession.title
        sessionDesc.text = maahitaSession.description
        sessionPresenter.text = maahitaSession.presenter.displayName
        var formatter = SimpleDateFormat("dd")
        sessionDay.text = formatter.format(maahitaSession.sessiondate)
        formatter = SimpleDateFormat("MMM")
        sessionMonth.text = formatter.format(maahitaSession.sessiondate)
        formatter = SimpleDateFormat("hh:mm a")
        sessionTime.text = formatter.format(maahitaSession.sessiondate)
        sessionStatus.text = maahitaSession.status.toString()
        sessionIndicator.backgroundTintList = ColorStateList.valueOf(maahitaSession.colorIndicator)

        if(maahitaSession.groupName.isNotEmpty()) {
            themeLabel.text = maahitaSession.groupName
            themeLabel.setTextColor(Color.WHITE)
            themeLabel.setBackgroundResource(R.drawable.rounded_background)
        } else {
            themeLabel.text = maahitaSession.sessiontheme
            themeLabel.setTextColor(Color.BLACK)
            themeLabel.setBackgroundResource(R.drawable.rounded_background_gray)
        }

        itemView.setOnClickListener {
            clickListener.onItemClicked(maahitaSession)
        }
    }
}

class SessionRecyclerAdapter(
    var items: List<MaahitaSession>,
    val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<SessionViewHolder>() {
    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionViewHolder {
        return SessionViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.layout_session_list_item,
                    parent,
                    false
                )
        )
    }

    override fun onBindViewHolder(holder: SessionViewHolder, position: Int) {
        when (holder) {
            is SessionViewHolder -> {
                holder.bind(items[position], itemClickListener)
            }
        }
    }
}

interface OnItemClickListener {
    fun onItemClicked(maahitaSession: MaahitaSession)
}