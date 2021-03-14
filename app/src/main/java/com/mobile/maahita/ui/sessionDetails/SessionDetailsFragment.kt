package com.mobile.maahita.ui.sessionDetails

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.dynamiclinks.ShortDynamicLink
import com.google.firebase.dynamiclinks.ktx.*
import com.google.firebase.ktx.Firebase
import com.mobile.maahita.R
import com.mobile.maahita.models.MaahitaSession
import com.mobile.maahita.recycleradapters.OnItemClickListener
import com.mobile.maahita.ui.presentersessions.PresenterSessions
import com.mobile.maahita.ui.sessionpresenter.SessionPresenterFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_session_details.*
import kotlinx.android.synthetic.main.fragment_session_details.view.*
import org.jitsi.meet.sdk.JitsiMeetActivity
import java.util.*


class SessionDetailsFragment : Fragment(),
    OnItemClickListener {

    private lateinit var viewModel: SessionDetailsViewModel
    private lateinit var sessionID: String
    private var isPrivate : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        childFragmentManager.fragmentFactory = SessionFragmentFactory()
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var toolbar = view?.toolbar
        toolbar?.setNavigationIcon(R.drawable.ic_action_back);
        toolbar?.setOnClickListener {
            activity?.onBackPressed()
        }

        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            progressbar.visibility = if (it) View.VISIBLE else View.GONE
        })

        attendsession.setOnClickListener {
            if (viewModel.isUserLogin())
                if (viewModel.IsReadyToLaunch() == true)
                    launchMeeting()
                else {
                    viewModel.enroll()
                }
            else {
                val bundle: Bundle = bundleOf(
                    "sessionID" to sessionID,
                    "sessionCommand" to (if (viewModel.IsReadyToLaunch() == true)  "Attend" else "Enroll")
                )
                findNavController().navigate(R.id.action_sessiondetails_to_login, bundle)
            }
        }

        sharesession.setOnClickListener {
            val session = viewModel.session.value

            session?.let {
                Firebase.dynamicLinks.shortLinkAsync(ShortDynamicLink.Suffix.SHORT) {
                    link = Uri.parse("https://mobile.maahita.com/sess?=" + session.sessionId)
                    domainUriPrefix = "https://mobile.maahita.com"
                    // Open links with this app on Android
                    androidParameters("com.mobile.maahita") { minimumVersion = 10 }
                    // Open links with com.example.ios on iOS
                    iosParameters("com.mobile.maahita") { minimumVersion = "1.2" }
                }.addOnSuccessListener { shortDynamicLink ->
                    val intent = Intent()
                    intent.action = Intent.ACTION_SEND
                    intent.putExtra(
                        Intent.EXTRA_TEXT,
                        "Check out this interesting session from māhita \nTopic: ${session.title} \n${session.description} \nPresented by ${session.presenter.displayName} \n ${shortDynamicLink.shortLink}"
                    )
                    intent.type = "text/plain"
                    startActivity(Intent.createChooser(intent, "Share To:"))
                }
            }
        }

        addtocalendar.setOnClickListener {
            addtoCalendar()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val view = inflater.inflate(R.layout.fragment_session_details, container, false)

        viewModel = ViewModelProviders.of(this).get(SessionDetailsViewModel::class.java)

        val autoenroll = arguments?.getString("autoentroll")
        val sessionCommand = arguments?.getString("sessionCommand")
        if (autoenroll != null) {
            viewModel.getSession(autoenroll)
        } else {
            var sessionID = arguments?.getString("sessionID") ?: ""
            isPrivate = arguments?.getBoolean("isPrivate") ?: false
            val groupID = arguments?.getString("groupID") ?: ""

            viewModel.getSession(sessionID, isPrivate, groupID)
        }

        viewModel.session.observe(viewLifecycleOwner, Observer {

            this.sessionID = it.sessionId
            view.session_title.text = it.title
            view.session_description.text = it.description
            view.session_presenter.text = it.presenter.displayName
            view.session_date.text = viewModel.getSessionDate()

            if (it.groupName.isNotEmpty()) {
                view.themenamelabel.text = it.groupName
                view.themenamelabel.setTextColor(Color.WHITE)
                view.themenamelabel.setBackgroundResource(R.drawable.rounded_background)
            } else {
                view.themenamelabel.text = it.sessiontheme
                view.themenamelabel.setTextColor(Color.BLACK)
                view.themenamelabel.setBackgroundResource(R.drawable.rounded_background_gray)
            }

            autoenroll?.let {
                if (sessionCommand == "Enroll") {
                    viewModel.enroll()
                } else {
                    launchMeeting()
                }
            }

            val transaction = childFragmentManager.beginTransaction()

            if (it.amIpresenter) {
                transaction.add(
                    R.id.fragmentContainerView,
                    SessionPresenterFragment.newInstance(it.sessionId, it.isprivate)
                )
            } else {
                transaction.add(
                    R.id.fragmentContainerView,
                    PresenterSessions.newInstance(it.presenter.presenterId, it.sessionId)
                )
            }

            transaction.commit()
        })

        viewModel.enrolledButtonBehaviour.observe(viewLifecycleOwner, Observer {
            view.attendsession.text = it.displayText
            view.attendsession.isClickable = it.isEnabled
            view.attendsession.isEnabled = it.isEnabled

            view.attendsession.visibility = when (it.showButton) {
                true -> View.VISIBLE
                false -> View.INVISIBLE
            }
        })

        viewModel.sharebuttonBehaviour.observe(viewLifecycleOwner, Observer {
            view.sharesession.text = it.displayText
            view.sharesession.isClickable = it.isEnabled
            view.sharesession.visibility = when (it.showButton) {
                true -> View.VISIBLE
                false -> View.INVISIBLE
            }
            view.addtocalendar.visibility = when (it.showButton) {
                true -> View.VISIBLE
                false -> View.INVISIBLE
            }
        })

        viewModel.presenterAvatar.observe(viewLifecycleOwner, Observer { avatarURL ->
            if(!avatarURL.isNullOrEmpty()) {
                val url = Uri.parse(avatarURL)
                updatePhoto(url)
            }
        })

        viewModel.enrollStatusCode.observe(viewLifecycleOwner, Observer {
            var text = when (it) {
                EnrollStatusCode.Fail -> "Oops! Something went wrong. Please try again"
                EnrollStatusCode.Success -> "Congratulations! you are successfully enrolled to the session"
                EnrollStatusCode.Exist -> "You have been already enrolled for this session. We will notify you when the session starts."
            }
            Snackbar.make(view, text, Snackbar.LENGTH_LONG).show()

            if (it == EnrollStatusCode.Success) {
                addtoCalendar()
            }
        })
        return view
    }

    private fun addtoCalendar() {
        val session = viewModel.session.value
        session?.let {
            val beginTime: Calendar = Calendar.getInstance()
            beginTime.time = session.sessiondate
            val endTime: Calendar = Calendar.getInstance()
            endTime.time = session.sessiondate
            endTime.add(Calendar.MINUTE, 60)

            val intent: Intent = Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(
                    CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                    beginTime.timeInMillis
                )
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.timeInMillis)
                .putExtra(CalendarContract.Events.TITLE, session.title)
                .putExtra(CalendarContract.Events.DESCRIPTION, session.description)
                .putExtra(
                    CalendarContract.Events.AVAILABILITY,
                    CalendarContract.Events.AVAILABILITY_BUSY
                )
            startActivity(intent)
        }
    }


    private fun launchMeeting() {
        viewModel.attend()?.addOnSuccessListener {
            JitsiMeetActivity.launch(context, viewModel.getMeetingOptions())
        }?.addOnFailureListener {
            //TODO:
        }
    }

    private fun updatePhoto(photoUrl: Uri?) {
        photoUrl?.let {
            Picasso.get().load(photoUrl).noFade().into(presenter_avatar)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onItemClicked(maahitaSession: MaahitaSession) {
        viewModel.getSession(maahitaSession.sessionId)
    }
}