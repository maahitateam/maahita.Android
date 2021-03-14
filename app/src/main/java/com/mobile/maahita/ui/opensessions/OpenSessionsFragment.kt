package com.mobile.maahita.ui.opensessions

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.mobile.maahita.R
import com.mobile.maahita.models.MaahitaSession
import com.mobile.maahita.recycleradapters.OnItemClickListener
import com.mobile.maahita.recycleradapters.SessionRecyclerAdapter
import kotlinx.android.synthetic.main.open_sessions_fragment.*

class OpenSessionsFragment : Fragment(), OnItemClickListener {
    companion object {
        fun newInstance() = OpenSessionsFragment()
    }

    private lateinit var viewModel: OpenSessionsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        var openSessionsFragmentView = inflater.inflate(R.layout.open_sessions_fragment, container, false)
        viewModel = ViewModelProviders.of(this).get(OpenSessionsViewModel::class.java)
        return openSessionsFragmentView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.maahitaSessions.observe(viewLifecycleOwner, Observer {
            it
            sessionslistRecycler.adapter =
                SessionRecyclerAdapter(
                    it,
                    this
                )

            openprogressbar.visibility = if (viewModel.IsLoading()) View.VISIBLE else View.GONE

            if (it.count() > 0) {
                opennodatalabel?.visibility = View.GONE
            } else {
                opennodatalabel?.visibility = View.VISIBLE
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getSessions()
        sessionslistRecycler.apply {
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onItemClicked(maahitaSession: MaahitaSession) {

        val bundle: Bundle = bundleOf(
            "sessionID" to maahitaSession.sessionId
        )
        findNavController().navigate(R.id.sessionDetailsFragment, bundle)
    }
}
