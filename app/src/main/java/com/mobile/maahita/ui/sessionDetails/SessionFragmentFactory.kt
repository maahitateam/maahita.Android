package com.mobile.maahita.ui.sessionDetails

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.mobile.maahita.ui.presentersessions.PresenterSessions
import com.mobile.maahita.ui.sessionpresenter.SessionPresenterFragment

class SessionFragmentFactory: FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when(loadFragmentClass(classLoader, className)) {
            PresenterSessions::class.java -> PresenterSessions()
            SessionPresenterFragment::class.java -> SessionPresenterFragment()
            else -> super.instantiate(classLoader, className)
        }
    }
}