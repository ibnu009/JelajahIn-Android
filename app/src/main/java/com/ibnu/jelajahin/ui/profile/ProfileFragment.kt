package com.ibnu.jelajahin.ui.profile

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.ibnu.jelajahin.R
import com.ibnu.jelajahin.core.data.model.User
import com.ibnu.jelajahin.core.data.remote.network.ApiResponse
import com.ibnu.jelajahin.core.utils.JelajahinConstValues.KEY_TOKEN
import com.ibnu.jelajahin.core.utils.SharedPreferenceManager
import com.ibnu.jelajahin.databinding.ProfileFragmentBinding
import com.ibnu.jelajahin.extention.getUserLevel
import com.ibnu.jelajahin.extention.getUserLevelProgressInPercent
import com.ibnu.jelajahin.extention.getUserLevelProgressInPercentAsInt
import com.ibnu.jelajahin.extention.popTap
import com.ibnu.jelajahin.utils.UiConstValue.FAST_ANIMATION_TIME
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ProfileFragment : Fragment(), View.OnClickListener {

    private val viewModel: ProfileViewModel by viewModels()

    private var _binding: ProfileFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var preference: SharedPreferenceManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ProfileFragmentBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preference = SharedPreferenceManager(requireContext())
        val token = preference.getToken ?: ""
        if (token == "") {
            initiateNotLoggedUser()
        } else {
            binding.contentProfile.visibility = View.VISIBLE
            binding.contentNotUser.visibility = View.GONE

            viewModel.getUserProfile(token).observe(viewLifecycleOwner, Observer { response ->
                when(response){
                    is ApiResponse.Loading -> {
                        Timber.d("Loading")
                    }
                    is ApiResponse.Error -> {
                        Timber.d("Error ${response.errorMessage}")
                    }
                    is ApiResponse.Success -> {
                        initiateProfileView(response.data)
                    }
                    else -> {
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

    private fun initiateProfileView(user: User) {
        binding.tvName.text = user.fullName
        binding.tvEmail.text = user.email

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

        binding.btnGoToLogin.setOnClickListener {
            it.popTap()
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
            }, FAST_ANIMATION_TIME)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(p0: View?) {
        when(p0) {
            binding.profileComponent.layoutLogout -> {
                p0.popTap()
                Handler(Looper.getMainLooper()).postDelayed({
                    preference.clearPreferenceByKey(KEY_TOKEN)
                    findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
                }, FAST_ANIMATION_TIME)
            }
            binding.profileComponent.layoutAbout -> {
                p0.popTap()
                Timber.d("Menekan layout about")
            }
            binding.profileComponent.layoutEditProfile -> {
                p0.popTap()
                Timber.d("Menekan layout edit profile")
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
                Timber.d("Menekan layout history")
            }
            binding.profileComponent.layoutShop -> {
                p0.popTap()
                Timber.d("Menekan layout shop")
            }
            binding.profileComponent.layoutSyaratDanKetentuan -> {
                p0.popTap()
                Timber.d("Menekan layout syarat dan ketentuan")
            }
        }
    }

}