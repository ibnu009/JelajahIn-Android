package com.ibnu.jelajahin.ui.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.ibnu.jelajahin.R
import com.ibnu.jelajahin.core.data.model.User
import com.ibnu.jelajahin.core.data.remote.network.ApiResponse
import com.ibnu.jelajahin.core.extention.*
import com.ibnu.jelajahin.core.utils.JelajahinConstValues.BASE_URL
import com.ibnu.jelajahin.core.utils.JelajahinConstValues.KEY_EMAIL
import com.ibnu.jelajahin.core.utils.JelajahinConstValues.KEY_TOKEN
import com.ibnu.jelajahin.core.utils.SharedPreferenceManager
import com.ibnu.jelajahin.databinding.ProfileFragmentBinding
import com.ibnu.jelajahin.utils.UiConstValue.FAST_ANIMATION_TIME
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ProfileFragment : Fragment(), View.OnClickListener {

    private val viewModel: ProfileViewModel by viewModels()

    private var _binding: ProfileFragmentBinding? = null
    private val binding get() = _binding!!

    lateinit var pref: SharedPreferenceManager

    private var token: String = ""
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().showExitJelajahInDialog()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ProfileFragmentBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref = SharedPreferenceManager(requireContext())
        token = pref.getToken ?: ""
        Timber.d("Token is $token")
        if (token == "") {
            initiateNotLoggedUser()
        } else {
            viewModel.getUserProfile(token).observe(viewLifecycleOwner, { response ->
                when (response) {
                    is ApiResponse.Loading -> {
                        Timber.d("Loading")
                        binding.progressBar.visibility = View.VISIBLE
                        binding.contentProfile.visibility = View.GONE
                        binding.contentNotUser.visibility = View.GONE
                    }
                    is ApiResponse.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Timber.d("Error ${response.errorMessage}")
                    }
                    is ApiResponse.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.contentProfile.visibility = View.VISIBLE
                        binding.contentNotUser.visibility = View.GONE
                        initiateProfileView(response.data)
                        user = response.data
                    }
                    else -> {
                        binding.progressBarUser.visibility = View.GONE
                        Timber.d("Unknown Error")
                    }
                }
            })

            binding.profileComponent.layoutLogout.setOnClickListener(this)
            binding.profileComponent.layoutAbout.setOnClickListener(this)
            binding.profileComponent.layoutHelp.setOnClickListener(this)
            binding.profileComponent.layoutEditProfile.setOnClickListener(this)
            binding.profileComponent.layoutFaq.setOnClickListener(this)
            binding.profileComponent.layoutHistoryPoint.setOnClickListener(this)
            binding.profileComponent.layoutShop.setOnClickListener(this)
            binding.profileComponent.layoutSyaratDanKetentuan.setOnClickListener(this)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initiateProfileView(user: User) {
        binding.tvName.text = user.fullName
        binding.tvEmail.text = user.email

        Glide.with(binding.root)
            .load(BASE_URL+user.imageUrl)
            .placeholder(R.drawable.img_person)
            .into(binding.imgPhotoProfile)

        binding.tvAppreciationTotal.text = "${user.totalAppreciations} Apresiasi"
        binding.tvEventTotal.text = "${user.totalEvents} Event dihadiri"
        binding.tvUlasanTotal.text = "${user.totalReviews} ulasan"
        binding.tvPoin.text = "${user.totalPoints}"
        binding.tvProgressPercent.text = user.totalXp.getUserLevelProgressInPercent()
        binding.tvUserLevel.text = user.totalXp.getUserLevel()
        binding.progressBarUser.progress = user.totalXp.getUserLevelProgressInPercentAsInt()
    }

    private fun initiateNotLoggedUser() {
        binding.contentProfile.visibility = View.GONE
        binding.contentNotUser.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE

        binding.btnGoToLogin.setOnClickListener {
            it.popTap()
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
            }, FAST_ANIMATION_TIME)
        }
    }

    override fun onClick(p0: View?) {
        when(p0) {
            binding.profileComponent.layoutLogout -> {
                p0.popTap()
                Handler(Looper.getMainLooper()).postDelayed({
                    logoutDialog()
                }, FAST_ANIMATION_TIME)
            }
            binding.profileComponent.layoutAbout -> {
                p0.popTap()
                Timber.d("Menekan layout about")
            }
            binding.profileComponent.layoutEditProfile -> {
                p0.popTap()
                Timber.d("Menekan layout edit profile")
                Handler(Looper.getMainLooper()).postDelayed({
                    val action = ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment(user)
                    findNavController().navigate(action)
                }, FAST_ANIMATION_TIME)
            }
            binding.profileComponent.layoutFaq -> {
                p0.popTap()
                Timber.d("Menekan layout FAQ")
            }
            binding.profileComponent.layoutHelp -> {
                p0.popTap()
                Timber.d("Menekan layout help")
            }
            binding.profileComponent.layoutHistoryPoint -> {
                p0.popTap()
                Handler(Looper.getMainLooper()).postDelayed({
                    val action = ProfileFragmentDirections.actionProfileFragmentToPointHistoryFragment(user.totalPoints)
                    findNavController().navigate(action)
                }, FAST_ANIMATION_TIME)
            }
            binding.profileComponent.layoutShop -> {
                p0.popTap()
                Handler(Looper.getMainLooper()).postDelayed({
                    findNavController().navigate(R.id.action_profileFragment_to_shopFragment)
                }, FAST_ANIMATION_TIME)
            }
            binding.profileComponent.layoutSyaratDanKetentuan -> {
                p0.popTap()
                Timber.d("Menekan layout syarat dan ketentuan")
            }
        }
    }

    private fun logoutDialog() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle("Logout")
            setMessage("Apakah Anda yakin untuk logout?")
            setNegativeButton("Tidak") { p0, _ ->
                p0.dismiss()
            }
            setPositiveButton("IYA") { _, _ ->
                try {
                    pref.clearPreferenceByKey(KEY_TOKEN)
                    pref.clearPreferenceByKey(KEY_EMAIL)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
                }
            }
        }.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}