package com.ibnu.jelajahin.ui.restaurant

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
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.ibnu.jelajahin.R
import com.ibnu.jelajahin.core.data.model.Restaurant
import com.ibnu.jelajahin.core.extention.*
import com.ibnu.jelajahin.core.ui.state.PostStateHandler
import com.ibnu.jelajahin.core.utils.JelajahinConstValues
import com.ibnu.jelajahin.core.utils.SharedPreferenceManager
import com.ibnu.jelajahin.databinding.FragmentUlasanRestaurantBinding
import com.ibnu.jelajahin.utils.UiConstValue
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UlasanRestaurantFragment : Fragment(), PostStateHandler {

    private val viewModel: RestaurantViewModel by viewModels()

    private var _binding: FragmentUlasanRestaurantBinding? = null
    private val binding get() = _binding!!

    private var imagePath: String? = null
    private lateinit var pref: SharedPreferenceManager
    private lateinit var restaurant: Restaurant

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUlasanRestaurantBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initiatePermission(requireActivity())

        val safeArgs = arguments?.let { UlasanRestaurantFragmentArgs.fromBundle(it) }
        restaurant = safeArgs?.restaurant!!

        pref = SharedPreferenceManager(requireContext())
        val token: String = pref.getToken ?: ""

        viewModel.postState = this

        binding.tvUlasanName.text = restaurant.name
        Glide.with(binding.root)
            .load("${JelajahinConstValues.BASE_URL}${restaurant.imageUrl}")
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
            val ratingService = binding.rbUlasanService.rating.toInt()
            val ratingFood = binding.rbUlasanFood.rating.toInt()
            val ratingClean = binding.rbUlasanClean.rating.toInt()

            when {
                title.isEmpty() -> {
                    binding.edtUlasanTitle.error = "Tidak boleh kosong"
                }
                content.isEmpty() -> {
                    binding.edtUlasanContent.error = "Tidak boleh kosong"
                }
                ratingService == 0 || ratingFood == 0 || ratingClean == 0 -> {
                    binding.root.snackBar("Masih ada Rating yang kosong!")
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
                        ratingService,
                        ratingFood,
                        ratingClean,
                        restaurant.uuidRestaurant,
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
            if (bitmap != null) {
                val uri = requireActivity().getImageUri(bitmap)
                imagePath = requireActivity().getFilePathFromUri(uri)
                binding.imgUlasan.setImageBitmap(bitmap)
            }
        }

    private val pickFileImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                imagePath = requireActivity().getFilePathFromUri(uri)
                binding.imgUlasan.setImageURI(uri)
            }
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