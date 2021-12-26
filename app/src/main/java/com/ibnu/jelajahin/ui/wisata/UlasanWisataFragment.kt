package com.ibnu.jelajahin.ui.wisata

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
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.ibnu.jelajahin.R
import com.ibnu.jelajahin.core.data.model.Wisata
import com.ibnu.jelajahin.core.extention.*
import com.ibnu.jelajahin.core.ui.state.PostStateHandler
import com.ibnu.jelajahin.core.utils.JelajahinConstValues
import com.ibnu.jelajahin.core.utils.SharedPreferenceManager
import com.ibnu.jelajahin.databinding.FragmentUlasanWisataBinding
import com.ibnu.jelajahin.utils.UiConstValue
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UlasanWisataFragment : Fragment(), PostStateHandler {

    private val viewModel: WisataViewModel by viewModels()

    private var _binding: FragmentUlasanWisataBinding? = null
    private val binding get() = _binding!!

    private var imagePath: String? = null
    private lateinit var pref: SharedPreferenceManager
    private lateinit var wisata: Wisata

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUlasanWisataBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initiatePermission(requireActivity())

        val safeArgs = arguments?.let { UlasanWisataFragmentArgs.fromBundle(it) }
        wisata = safeArgs?.wisata!!

        pref = SharedPreferenceManager(requireContext())
        val token: String = pref.getToken ?: ""

        viewModel.postState = this

        binding.tvUlasanName.text = wisata.name
        Glide.with(binding.root)
            .load( wisata.imageUrl)
            .placeholder(R.drawable.skeleton)
            .into(binding.ivUlasan)

        binding.imgUlasan.setOnClickListener {
            it.popTap()
            Handler(Looper.getMainLooper()).postDelayed({
                showImageMenu()
            }, UiConstValue.FAST_ANIMATION_TIME)
        }

        binding.btnSend.setOnClickListener {
            val title = binding.edtUlasanTitle.text.toString()
            val content = binding.edtUlasanContent.text.toString()
            val rating = binding.rbUlasan.rating.toInt()

            when {
                title.isEmpty() -> {
                    binding.edtUlasanTitle.error = "Tidak boleh kosong"
                }
                content.isEmpty() -> {
                    binding.edtUlasanContent.error = "Tidak boleh kosong"
                }
                rating == 0 -> {
                    binding.root.snackBar("Rating tidak boleh kosong!")
                }
                imagePath.isNullOrEmpty() -> {
                    binding.root.snackBar("Image tidak boleh kosong!")
                }
                else -> {
                    viewModel.uploadUlasan(
                        requireContext(),
                        viewLifecycleOwner,
                        token,
                        title,
                        content,
                        rating,
                        wisata.uuidWisata,
                        imagePath ?: ""
                    )
                }
            }
        }
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

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.bgDim.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private val takePictureRegistration =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            val uri = requireContext().getImageUri(bitmap)
            imagePath = requireContext().getFilePathFromUri(uri)
            binding.imgUlasan.setImageBitmap(bitmap)
        }

    private val pickFileImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            imagePath = requireContext().getFilePathFromUri(uri)
            binding.imgUlasan.setImageURI(uri)
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

    override fun onInitiating() {
        showLoading(true)
    }

    override fun onSuccess(message: String) {
        showLoading(false)
        showSuccessDialog(message)
    }

    private fun showSuccessDialog(message: String) {
        AlertDialog.Builder(requireContext()).apply {
            setTitle("Success!")
            setMessage(message)
            setPositiveButton("Ok") { p0, _ ->
                p0.dismiss()
                findNavController().popBackStack()
            }
        }.create().show()
    }

    override fun onFailure(message: String) {
        showLoading(false)
        requireContext().showOKDialog("Gagal", message)
    }


}