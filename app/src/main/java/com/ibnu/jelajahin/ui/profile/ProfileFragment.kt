package com.ibnu.jelajahin.ui.profile

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ibnu.jelajahin.R
import com.ibnu.jelajahin.core.data.model.User
import com.ibnu.jelajahin.core.utils.JelajahinConstValues.KEY_TOKEN
import com.ibnu.jelajahin.core.utils.SharedPreferenceManager
import com.ibnu.jelajahin.databinding.ProfileFragmentBinding
import com.ibnu.jelajahin.extention.getUserLevel
import com.ibnu.jelajahin.extention.getUserLevelProgressInPercent
import com.ibnu.jelajahin.extention.popTap
import com.ibnu.jelajahin.utils.UiConstValue.FAST_ANIMATION_TIME
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

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

            binding.profileComponent.layoutLogout.setOnClickListener {
                it.popTap()
                Handler(Looper.getMainLooper()).postDelayed({
                    preference.clearPreferenceByKey(KEY_TOKEN)
                    findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
                }, FAST_ANIMATION_TIME)

            }
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

}