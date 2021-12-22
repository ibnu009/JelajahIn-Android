package com.ibnu.jelajahin.ui.wisata

import android.content.Context
import android.content.res.Resources
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
import com.ibnu.jelajahin.R
import com.ibnu.jelajahin.core.extention.getFilePathFromUri
import com.ibnu.jelajahin.core.extention.getImageUri
import com.ibnu.jelajahin.core.extention.popTap
import com.ibnu.jelajahin.core.extention.snackBar
import com.ibnu.jelajahin.databinding.FragmentUlasanWisataBinding
import com.ibnu.jelajahin.utils.UiConstValue
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UlasanWisataFragment : Fragment() {

    private val viewModel: WisataViewModel by viewModels();

    private var _binding: FragmentUlasanWisataBinding? = null
    private val binding get() = _binding!!

    private var imagePath: String? = null

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

        binding.imgUlasan.setOnClickListener {
            it.popTap()
            Handler(Looper.getMainLooper()).postDelayed({
                showImageMenu()
            }, UiConstValue.FAST_ANIMATION_TIME)
        }

        binding.btnSend.setOnClickListener {
            val title = binding.edtUlasanTitle.text.toString()
            val content = binding.edtUlasanContent.text.toString()
        }
    }

    private fun showImageMenu() {
        AlertDialog.Builder(requireActivity())
            .setTitle("Pilih metode pengambilan Gambar")
            .setItems(
                Resources.getSystem().getStringArray(R.array.pictures)
            ) { _, p1 ->
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
            binding.imgUlasan.setImageBitmap(bitmap)
        }

    private val pickFileImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            imagePath = requireActivity().getFilePathFromUri(uri)
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

}