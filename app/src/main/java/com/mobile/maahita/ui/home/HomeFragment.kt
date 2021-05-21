package com.mobile.maahita.ui.home

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.mobile.maahita.MeetingManager
import com.mobile.maahita.R
import com.mobile.maahita.models.DashboardData
import com.mobile.maahita.models.FeedbackRequest
import com.mobile.maahita.models.LiveSessions
import com.mobile.maahita.models.MaahitaSession
import com.mobile.maahita.recycleradapters.DataAdapter
import com.mobile.maahita.recycleradapters.OnDashboardItemClickListener
import com.mobile.maahita.recycleradapters.OnItemClickListener
import com.mobile.maahita.recycleradapters.SessionRecyclerAdapter
import com.mobile.maahita.ui.opensessions.OpenSessionsFragment
import com.mobile.maahita.ui.privatesessions.PrivateSessionsFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.open_sessions_fragment.*
import org.jitsi.meet.sdk.JitsiMeetActivity
import java.lang.Double
import kotlin.math.ceil

class HomeFragment : Fragment(), OnDashboardItemClickListener {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        homeViewModel.getdashboardData()

        homeViewModel.userDashboardData.observe(viewLifecycleOwner, Observer {
            it
            dashboardRecycler.adapter = DataAdapter(it, this)
            recyclerViewIndicator.setItemCount(dashboardRecycler.childCount)
            recyclerViewIndicator.forceUpdateItemCount()

            if (recyclerViewIndicator.childCount > 1) {
                recyclerViewIndicator.visibility = View.VISIBLE
            } else {
                recyclerViewIndicator.visibility = View.GONE
            }
        })

        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(OpenSessionsFragment(), "Open Sessions")
        adapter.addFragment(PrivateSessionsFragment(), "Private Sessions")
        viewPager.adapter = adapter
        hometablayout.setupWithViewPager(viewPager)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        var fragmentHomeView = inflater.inflate(R.layout.fragment_home, container, false)

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        homeViewModel.setUserInfo()

        homeViewModel.loggedInUserView.observe(viewLifecycleOwner, Observer {
            if (it.isLoggedIn) {
                fragmentHomeView.loginButton.visibility = View.GONE
                updatePhoto(it.photoUri)
            } else {
                fragmentHomeView.loginButton.visibility = View.VISIBLE
                fragmentHomeView.profileview.setImageResource(R.drawable.user)
            }
            fragmentHomeView.user_display_name.text = it.displayName.toString()

            fragmentHomeView.user_email_id.text = it.emailId.toString().apply {
                if (!it.isEmailVerified) {
                    fragmentHomeView.user_email_id.setTextColor(Color.RED)
                } else {
                    fragmentHomeView.user_email_id.setTextColor(R.style.AppTheme)
                }
            }
        })

        return fragmentHomeView
    }

    private fun updatePhoto(photoUrl: Uri?) {
        if (photoUrl?.path != null) {
            val picasso = Picasso.get()
            picasso.load(photoUrl).noFade()
                .into(profileview)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dashboardRecycler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }

        PagerSnapHelper().attachToRecyclerView(dashboardRecycler)

        recyclerViewIndicator.setRecyclerView(dashboardRecycler)
        recyclerViewIndicator.setCurrentPosition(0)

        loginButton.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_to_login)
        }

        profileview.setOnClickListener {
            if (homeViewModel.IsUserLoggedIn()!!) {
                findNavController().navigate(R.id.action_to_profileSettings, null)
            } else {
                Snackbar.make(view, "Please login to update profile image", Snackbar.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onDashboardClicked(dashboardData: DashboardData) {
        TODO("Not yet implemented")
    }

    override fun onLiveClicked(liveSession: LiveSessions) {
        if (homeViewModel.IsUserLoggedIn() == true)
            homeViewModel.attendLive(liveSession.sessionId)?.addOnSuccessListener {
                val meetingManager = MeetingManager(this.requireContext())
                val meetingOptions =
                    meetingManager.getMeetingOptions(liveSession.meetingID, liveSession.title)
                meetingOptions?.let {
                    JitsiMeetActivity.launch(this.context, it)
                }
            }
        else {
            findNavController().navigate(R.id.action_sessiondetails_to_login)
        }
    }

    override fun onFeedbackClicked(feedback: FeedbackRequest) {
        val bundle: Bundle = bundleOf(
            "feedbackRequest" to feedback
        )
        findNavController().navigate(R.id.feedbacksubmitform, bundle)
    }

    class ViewPagerAdapter(supportFragmentManager: FragmentManager) : FragmentStatePagerAdapter(supportFragmentManager) {

        private val mFragmentList = ArrayList<Fragment>()
        private val mFragmentTitleList = ArrayList<String>()

        override fun getItem(position: Int): Fragment {
            return mFragmentList.get(position)
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mFragmentTitleList[position]
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }
    }
}
