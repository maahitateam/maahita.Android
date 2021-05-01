package com.mobile.maahita.ui.profile_settings

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.PopupMenu
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.mobile.maahita.R
import com.mobile.maahita.repository.FirestoreRepository
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile_settings.*
import kotlinx.android.synthetic.main.fragment_session_details.view.*
import java.io.ByteArrayOutputStream


class SettingsFragment : Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var storage: FirebaseStorage

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        storage = Firebase.storage
        var view = inflater.inflate(R.layout.fragment_profile_settings, container, false)
        settingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel::class.java)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadUI()

        backfloatingButton.setOnClickListener {
            activity?.onBackPressed()
        }

        openImageLoader.setOnClickListener {
            showMenu()
        }

        btnVerify.setOnClickListener {
            btnVerify.isClickable = false
            settingsViewModel.verifyEmailId()?.addOnSuccessListener {
                Snackbar.make(
                    view,
                    "Please check your email to verify the mailid",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        updateName.setOnClickListener {
            settingsViewModel.updateFullName(fullname.text.toString())
            Snackbar.make(
                view,
                "Your display name is save successfully",
                Snackbar.LENGTH_LONG
            ).show()
        }

        this.changepassword.setOnClickListener {
            settingsViewModel.changepassword(this.email.text.toString())?.addOnSuccessListener {
                Snackbar.make(
                    view,
                    "Password reset link has been sent to email",
                    Snackbar.LENGTH_LONG
                ).show()

            }?.addOnFailureListener {
                Snackbar.make(
                    view,
                    "Error occurred. Please reach the application support",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        this.removeAccountButton.setOnClickListener {
            context?.let { it1 ->
                MaterialAlertDialogBuilder(it1)
                    .setTitle("Account removal alert")
                    .setMessage("So sad, we will be missing you. Are you sure? You want to leave us?")
                    .setPositiveButton("Yes") { dialog, which ->
                        settingsViewModel.removeAccount()?.addOnSuccessListener {
                            Snackbar.make(
                                view,
                                "Your account has been removed.",
                                Snackbar.LENGTH_LONG
                            ).show()
                            findNavController().navigate(R.id.navigation_sessions)
                        }
                    }.setNegativeButton("No") { dialog, which ->
                        Snackbar.make(
                            view,
                            "Thanks for coming back. Enjoy learning with us",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }.show()
            }
        }

        logoutAccount.setOnClickListener {
            context?.let { it1 ->
                MaterialAlertDialogBuilder(it1)
                    .setTitle("Logout alert")
                    .setMessage("Are you sure? You want to logout?")
                    .setPositiveButton("Yes") { dialog, which ->
                        settingsViewModel.logout()
                        findNavController().navigate(R.id.navigation_sessions)
                    }.setNegativeButton("No") { dialog, which ->
                    }.show()
            }
        }
    }

    private fun loadUI() {
        progressbar.visibility = View.VISIBLE
        this.avatarimageview.setImageResource(R.drawable.user)

        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let {

            email.setText(it.email).apply {
                if (!it.isEmailVerified)
                    email.setTextColor(Color.RED)
                else btnVerify.visibility = View.GONE
            }
            fullname.setText(it.displayName)

            if (currentUser.photoUrl != null) {
                Picasso.get().load(currentUser.photoUrl).noFade().into(profile_back_image)
                Picasso.get().load(currentUser.photoUrl).noFade().into(avatarimageview)
            }
        }
        progressbar.visibility = View.GONE
    }

    fun showMenu() {
        val popup = PopupMenu(requireContext(), openImageLoader)
        popup.getMenuInflater().inflate(R.menu.imagedrawermenu, popup.getMenu())
        popup.setOnMenuItemClickListener {
            if (it.itemId == R.id.navigation_camera_open) {
                dispatchTakePictureIntent()
                true
            } else {
                openImageAlbum()
                true
            }
        }
        popup.show()
    }

    private val CAMERA_REQUEST_CODE = 100
    private val ALBUM_REQUEST_CODE = 200

    private fun dispatchTakePictureIntent() {

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    (context as Activity?)!!,
                    Manifest.permission.CAMERA
                )
            ) {
            } else {
                ActivityCompat.requestPermissions(
                    (context as Activity?)!!,
                    arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE
                )
            }
        }

        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                startActivityForResult(takePictureIntent, 2000)
            }
        }
    }

    private fun openImageAlbum() {

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    (context as Activity?)!!,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
            } else {
                ActivityCompat.requestPermissions(
                    (context as Activity?)!!,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), ALBUM_REQUEST_CODE
                )
            }
        }

        val pickPhoto = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(pickPhoto, 5000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        progressbar.visibility = View.VISIBLE
        if ((requestCode == 2000 || requestCode == 5000) && resultCode == RESULT_OK) {

            var imageBitmap: Bitmap? = null
            if (requestCode == 2000) {
                imageBitmap = data?.extras?.get("data") as? Bitmap
            } else {
                data?.data?.let {
                    imageBitmap = getCapturedImage(it)
                }
            }
            imageBitmap?.let {
                val storageRef = storage.reference

                val baos = ByteArrayOutputStream()
                it.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()

                val filename = FirebaseAuth.getInstance().currentUser!!.uid

                var uploadRef = storageRef.child("avatars/" + filename + ".jpg")
                uploadRef.putBytes(data).continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    } else {
                        uploadRef.downloadUrl
                    }
                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result
                        downloadUri?.let {
                            val profileUpdates = UserProfileChangeRequest
                                .Builder()
                                .setPhotoUri(downloadUri)
                                .build()

                            FirebaseAuth.getInstance().currentUser?.updateProfile(
                                profileUpdates
                            )?.addOnSuccessListener { updated ->
                                settingsViewModel.updateAvatar(downloadUri.path!!)
                                loadUI()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getCapturedImage(selectedPhotoUri: Uri): Bitmap {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source =
                ImageDecoder.createSource(requireContext().contentResolver, selectedPhotoUri)
            return ImageDecoder.decodeBitmap(source)
        } else {
            return MediaStore.Images.Media.getBitmap(
                requireContext().contentResolver,
                selectedPhotoUri
            )
        }
    }
}