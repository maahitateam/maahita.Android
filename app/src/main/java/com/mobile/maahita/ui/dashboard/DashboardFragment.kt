package com.mobile.maahita.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.mobile.maahita.recycleradapters.OnItemClickListener
import com.mobile.maahita.R
import com.mobile.maahita.models.MaahitaSession
import com.mobile.maahita.ui.home.HomeViewModel

class DashboardFragment : Fragment(),
    OnItemClickListener {

    private lateinit var dashboardViewModel: DashboardViewModel

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        //val recyclerView: RecyclerView = root.findViewById(R.id.recycler_View)
        //homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        recycler_View.apply {
//            layoutManager = LinearLayoutManager(context)
//            val topSpacingDecorator = TopSpacingItemDecoration(30)
//            addItemDecoration(topSpacingDecorator)
//        }
//
//        homeViewModel.getSessions().observe(viewLifecycleOwner, Observer {
//            it
//            recycler_View.adapter = SessionRecyclerAdapter(it,this)
//        })
    }

    override fun onItemClicked(maahitaSession: MaahitaSession) {

        var bundle = bundleOf(
            "sessionid" to maahitaSession.sessionId,
            "title" to maahitaSession.title,
            "description" to maahitaSession.description,
            "date" to maahitaSession.sessiondate,
            "presenter" to maahitaSession.presenter,
            "meetingID" to maahitaSession.meetingID,
            "isenrolled" to maahitaSession.isEnrolled
        )

        findNavController().navigate(R.id.sessionDetailsFragment, bundle)
    }
}
