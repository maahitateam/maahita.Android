package com.mobile.maahita.ui.sessionpresenter

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

import com.mobile.maahita.R
import com.mobile.maahita.ui.sessionDetails.SessionDetailsViewModel
import kotlinx.android.synthetic.main.fragment_session_details.view.*
import kotlinx.android.synthetic.main.session_presenter_fragment.view.*

class SessionPresenterFragment : Fragment() {

    companion object {
        fun newInstance(sessionID: String, isPrivate: Boolean = false) =
            SessionPresenterFragment().apply {
                arguments = Bundle().apply {
                    putString("sessionID", sessionID)
                    putBoolean("isPrivate", isPrivate)
                }
            }
    }

    private lateinit var viewModel: SessionPresenterViewModel

    private lateinit var sessionID: String
    private var isPrivate: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionID = arguments?.getString("sessionID") ?: ""
        isPrivate = arguments?.getBoolean("isPrivate") ?: false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.session_presenter_fragment, container, false)
        viewModel = ViewModelProviders.of(this).get(SessionPresenterViewModel::class.java)

        viewModel.getSessionStats(sessionID)

        viewModel.numberofEnrollments.observe(viewLifecycleOwner, Observer {
            view.numberofenrollments.text = it.toString()
        })

        viewModel.numberofAttendees.observe(viewLifecycleOwner, Observer {
            view.numberofattended.text = it.toString()
        })

        viewModel.overallSessionRating.observe(viewLifecycleOwner, Observer {
            view.sessionrating.rating = it.toFloat()
        })
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SessionPresenterViewModel::class.java)
    }
}
