package com.mobile.maahita.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.mobile.maahita.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.view.*
import kotlinx.android.synthetic.main.activity_login.view.toolbar
import kotlinx.android.synthetic.main.fragment_session_details.view.*

class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var sessionId: String
    private lateinit var sessionCommand: String


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //
        var toolbar = view?.toolbar
        toolbar?.setNavigationIcon(R.drawable.ic_action_back);
        toolbar?.setOnClickListener {
            activity?.onBackPressed()
        }

        cancel_button.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        var view = inflater.inflate(R.layout.activity_login, container, false)

        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        sessionId = arguments?.getString("sessionID").toString()
        sessionCommand = arguments?.getString("sessionCommand").toString()

        view.login_button.setOnClickListener {

            if (!isUsernameValid(username_edit_text.text)) {
                username_text_input.error = getString(R.string.empty_username)
            }

            if (!isPasswordValid(password_edit_text.text)) {
                password_text_input.error = getString(R.string.user_password_error)
            } else {
                password_text_input.error = null // Clear the error

                viewModel.login(
                    username_edit_text.text.toString(),
                    password_edit_text.text.toString()
                )
            }
        }

        viewModel.loginResult.observe(viewLifecycleOwner, Observer {
            val loginResult = it ?: return@Observer

            if (loginResult.error != null) {
                login_error_label.setText(loginResult.error)
            }
            if (loginResult.success != null) {
                if (sessionId == "null") {
                    findNavController().navigate(R.id.navigation_sessions)
                } else {
                    val bundle: Bundle = bundleOf(
                        "autoentroll" to sessionId,
                        "sessionCommand" to sessionCommand
                    )
                    findNavController().navigate(
                        R.id.action_navigation_login_to_sessionDetails,
                        bundle
                    )
                }
            }
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnSignup.setOnClickListener {

            Navigation.findNavController(view)
                .navigate(R.id.action_loginFragment_to_fragment_signup)
        }
    }

    private fun isUsernameValid(text: Editable?): Boolean {
        return text != null //&& text.length >= 8
    }

    private fun isPasswordValid(text: Editable?): Boolean {
        return text != null && text.length >= 5
    }
}