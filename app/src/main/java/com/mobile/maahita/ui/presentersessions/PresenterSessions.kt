package com.mobile.maahita.ui.presentersessions

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.mobile.maahita.R
import com.mobile.maahita.models.MaahitaSession
import com.mobile.maahita.recycleradapters.OnItemClickListener
import com.mobile.maahita.recycleradapters.SessionRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_session_details.*
import kotlinx.android.synthetic.main.presenter_sessions_fragment.*
import kotlinx.android.synthetic.main.presenter_sessions_fragment.sessionslistRecycler
import kotlinx.android.synthetic.main.private_sessions_fragment.*


class PresenterSessions : Fragment(), OnItemClickListener {

    val PresenterID: String = "PresenterID"
    private lateinit var presenterId: String
    private lateinit var sessionId: String

    companion object {
        fun newInstance(presenterId: String, sessionId: String) = PresenterSessions().apply {
            arguments = Bundle().apply {
                putString(PresenterID, presenterId)
                putString("SessionID", sessionId)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.presenterId = arguments?.getString(PresenterID) ?: ""
        this.sessionId = arguments?.getString("SessionID") ?: ""
    }

    private lateinit var viewModel: PresenterSessionsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.presenter_sessions_fragment, container, false)
        viewModel = ViewModelProviders.of(this).get(PresenterSessionsViewModel::class.java)
        viewModel.presenterSessions.observe(viewLifecycleOwner, Observer {
            it
            sessionslistRecycler.adapter =
                SessionRecyclerAdapter(
                    it,
                    this
                )

            presentersessRwIndicator.setItemCount(sessionslistRecycler.childCount)
            presentersessRwIndicator.forceUpdateItemCount()

            if (presentersessRwIndicator.childCount > 1) {
                presentersessRwIndicator.visibility = View.VISIBLE
            } else {
                presentersessRwIndicator.visibility = View.GONE
            }
        })

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getSessionsByPresenter(presenterId, sessionId)
    }

    override fun onItemClicked(maahitaSession: MaahitaSession) {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionslistRecycler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

        PagerSnapHelper().attachToRecyclerView(sessionslistRecycler)

        presentersessRwIndicator.setRecyclerView(sessionslistRecycler)
        presentersessRwIndicator.setCurrentPosition(0)
    }
}
