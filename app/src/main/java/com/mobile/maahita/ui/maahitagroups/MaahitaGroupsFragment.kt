package com.mobile.maahita.ui.maahitagroups

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mobile.maahita.R

class MaahitaGroupsFragment : Fragment() {

    companion object {
        fun newInstance() = MaahitaGroupsFragment()
    }

    private lateinit var viewModel: MaahitaGroupsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.maahita_groups_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MaahitaGroupsViewModel::class.java)
        // TODO: Use the ViewModel
    }
}