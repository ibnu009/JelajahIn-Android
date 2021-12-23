package com.ibnu.jelajahin.ui.profile

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.ibnu.jelajahin.R
import com.ibnu.jelajahin.core.data.model.User
import com.ibnu.jelajahin.core.extention.*
import com.ibnu.jelajahin.core.utils.JelajahinConstValues
import com.ibnu.jelajahin.core.utils.SharedPreferenceManager
import com.ibnu.jelajahin.databinding.FragmentEditProfileBinding
import com.ibnu.jelajahin.utils.UiConstValue
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    private var imagePath: String? = null
    private lateinit var pref: SharedPreferenceManager
    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initiatePermission(requireActivity())
        pref = SharedPreferenceManager(requireContext())
        val token: String = pref.getToken ?: ""

        val safeArgs = arguments?.let { EditProfileFragmentArgs.fromBundle(it) }
        user = safeArgs?.user!!

        binding.edtFullName.setText(user.fullName)
        binding.edtEmail.setText(user.email)
        binding.edtAddress.setText(user.origin)

        Glide.with(binding.root)
            .load(JelajahinConstValues.BASE_URL +user.imageUrl)
            .placeholder(R.drawable.img_person)
            .into(binding.imgProfile)

        binding.imgProfile.setOnClickListener {
            it.popTap()
            Handler(Looper.getMainLooper()).postDelayed({
                showImageMenu()
            }, UiConstValue.FAST_ANIMATION_TIME)
        }

        binding.btnSend.setOnClickListener {
            it.popTap()
            Handler(Looper.getMainLooper()).postDelayed({
                sendProfileEdit(token)
            }, UiConstValue.FAST_ANIMATION_TIME)
        }
    }

    private fun sendProfileEdit(token: String) {
        val name = binding.edtFullName.text.toString()
        val address = binding.edtAddress.text.toString()
        val email = binding.edtEmail.text.toString()

        if (imagePath.isNullOrEmpty()) {
            requireContext().showOKDialog(
                "Image Kosong",
                "Gambar kamu kosong nih, silahkan ambil gambar dari kamera atau galeri"
            )
            return
        }

        viewModel.editUserProfile(
            requireActivity(),
            viewLifecycleOwner,
            token,
            name,
            email,
            address,
            imagePath ?: ""
        )
    }


    private fun showImageMenu() {
        AlertDialog.Builder(requireActivity())
            .setTitle("Pilih metode pengambilan Gambar")
            .setItems(R.array.pictures) { _, p1 ->
                if (p1 == 0) {
                    takePictureRegistration.launch()
                } else {
                    pickFileImage.launch("image/*")
                }
            }.create().show()
    }

    private val takePictureRegistration =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            val uri = requireActivity().getImageUri(bitmap)
            imagePath = requireActivity().getFilePathFromUri(uri)
            binding.imgProfile.setImageBitmap(bitmap)
        }

    private val pickFileImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            imagePath = requireActivity().getFilePathFromUri(uri)
            binding.imgProfile.setImageURI(uri)
        }

    private fun initiatePermission(context: Context) {
        Dexter.withContext(context)
            .withPermissions(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {}
                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    p1?.continuePermissionRequest()
                }
            }).withErrorListener {
                binding.root.snackBar("Aplikasi membutuhkan permission")
            }.onSameThread()
            .check()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}