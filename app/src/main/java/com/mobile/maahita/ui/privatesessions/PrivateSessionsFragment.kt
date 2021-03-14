package com.mobile.maahita.ui.privatesessions

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.mobile.maahita.R
import com.mobile.maahita.models.MaahitaSession
import com.mobile.maahita.recycleradapters.OnItemClickListener
import com.mobile.maahita.recycleradapters.SessionRecyclerAdapter
import kotlinx.android.synthetic.main.private_sessions_fragment.*

class PrivateSessionsFragment : Fragment(), OnItemClickListener {

    companion object {
        fun newInstance() = PrivateSessionsFragment()
    }

    private lateinit var viewModel: PrivateSessionsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        var fragmentHomeView =
            inflater.inflate(R.layout.private_sessions_fragment, container, false)
        viewModel = ViewModelProviders.of(this).get(PrivateSessionsViewModel::class.java)
        return fragmentHomeView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.getSessions()
        viewModel.maahitaSessions.observe(viewLifecycleOwner, Observer {
            it
            sessionslistRecycler.adapter =
                SessionRecyclerAdapter(
                    it,
                    this
                )

            privateprogressbar.visibility = if (viewModel.IsLoading()) View.VISIBLE else View.GONE

            if (it.count() > 0) {
                privatenodatalabel.visibility = View.GONE
            } else {
                privatenodatalabel.visibility = View.VISIBLE
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionslistRecycler.apply {
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onItemClicked(maahitaSession: MaahitaSession) {
        val bundle: Bundle = bundleOf(
            "sessionID" to maahitaSession.sessionId,
            "isPrivate" to true,
            "groupID" to maahitaSession.groupId
        )
        findNavController().navigate(R.id.sessionDetailsFragment, bundle)
    }
}
