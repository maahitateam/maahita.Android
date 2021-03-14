package com.mobile.maahita.ui.signup


import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.mobile.maahita.R
import com.mobile.maahita.databinding.ActivitySignUpBinding
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.cancel_button
import kotlinx.android.synthetic.main.fragment_profile_settings.*
import kotlinx.android.synthetic.main.fragment_session_details.view.*
import kotlin.math.sign

class SignUpFragment : Fragment() {


    private lateinit var signupViewModel: SignUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        var binding: ActivitySignUpBinding =
            DataBindingUtil.inflate(inflater, R.layout.activity_sign_up, container, false)

        signupViewModel =
            ViewModelProviders.of(this, SignupFragmentFactory()).get(SignUpViewModel::class.java)

        binding.loginViewModel = signupViewModel
        binding.lifecycleOwner = this

//Form valiation errors
        validateForm()

        signupViewModel.signupStatus.observe(viewLifecycleOwner, Observer {
            if (it) {
                findNavController().navigate(R.id.navigation_sessions, null)
            }
        })

        signupViewModel.resultData.observe(viewLifecycleOwner, Observer {
            var msg: String = "Oops! Something went wrong. Please try again"
            if (it) {
                msg =
                    "Congratulations! Registration is successful. Please login with your credentials"
            }
            Snackbar.make(
                binding.root,
                msg,
                Snackbar.LENGTH_SHORT
            ).show().run {
                signupViewModel.SetSignInStatus(true)
            }
//                .setAction("Action") {
//                    findNavController().navigate(R.id.navigation_sessions, null)
//                }.show()
        })

        return binding.root
    }

    private fun validateForm() {
        //TODO: optimize the code
        signupViewModel.passwordError.observe(viewLifecycleOwner, Observer {
            userpassword.error = it.toString()
        })

        signupViewModel.confPasswordError.observe(viewLifecycleOwner, Observer {
            cnfuserpassword.error = it.toString()
        })

        signupViewModel.emailError.observe(viewLifecycleOwner, Observer {
            useremail.error = it.toString()
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
        var toolbar = view?.toolbar
        toolbar?.setNavigationIcon(R.drawable.ic_action_back);
        toolbar?.setOnClickListener {
            activity?.onBackPressed()
        }

        cancel_button.setOnClickListener {
            activity?.onBackPressed()
        }
    }
}

class SignupFragmentFactory() : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SignUpViewModel() as T
    }

}
