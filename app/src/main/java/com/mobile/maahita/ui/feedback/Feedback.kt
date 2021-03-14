package com.mobile.maahita.ui.feedback

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar

import com.mobile.maahita.R
import com.mobile.maahita.TopSpacingItemDecoration
import com.mobile.maahita.models.FeedbackData
import com.mobile.maahita.models.FeedbackRequest
import com.mobile.maahita.recycleradapters.FeedbackRecyclerAdapter
import com.mobile.maahita.recycleradapters.OnItemSeekListener
import kotlinx.android.synthetic.main.feedback_fragment.*
import kotlinx.android.synthetic.main.feedback_fragment.view.*
import kotlinx.android.synthetic.main.feedback_fragment.view.toolbar
import kotlinx.android.synthetic.main.fragment_session_details.view.*

class Feedback() : Fragment(), OnItemSeekListener {

    companion object {
        fun newInstance() = Feedback()
    }

    private lateinit var viewModel: FeedbackViewModel
    private lateinit var feedbackRequest: FeedbackRequest

    constructor(parcel: Parcel) : this() {
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        var view = inflater.inflate(R.layout.feedback_fragment, container, false)

        viewModel = ViewModelProviders.of(this).get(FeedbackViewModel::class.java)

        feedbackRequest = arguments?.get("feedbackRequest") as FeedbackRequest

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var toolbar = view?.toolbar
        toolbar?.setNavigationIcon(R.drawable.ic_action_back);
        toolbar?.setOnClickListener {
            activity?.onBackPressed()
        }

        viewModel.getfeedbackQuestions().observe(viewLifecycleOwner, Observer {
            it
            feedbackrecycler.adapter = FeedbackRecyclerAdapter(it, this)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        feedback_session_title.text = feedbackRequest.title
        feedback_session_presenter.text = feedbackRequest.presenter.displayName

        feedbackrecycler.apply {
            layoutManager = LinearLayoutManager(context)
            val topSpacingDecorator = TopSpacingItemDecoration(30)
            addItemDecoration(topSpacingDecorator)
        }

        submit_feedback.setOnClickListener {
            viewModel.submitFeedback(feedbackRequest.sessionId)?.addOnSuccessListener {
                Snackbar.make(
                    view,
                    "Your feedback is submitted, thanks for your time",
                    Snackbar.LENGTH_LONG
                ).show()
                activity?.onBackPressed()
            }?.addOnFailureListener {
                Snackbar.make(
                    view,
                    "Oops! Something went wrong. Please try again",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onFeedbackSeeked(feedback: FeedbackData) {

    }
}
