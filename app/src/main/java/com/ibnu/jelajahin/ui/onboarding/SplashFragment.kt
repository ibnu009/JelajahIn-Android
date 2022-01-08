package com.ibnu.jelajahin.ui.onboarding

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ibnu.jelajahin.R
import com.ibnu.jelajahin.core.utils.SharedPreferenceManager
import com.ibnu.jelajahin.databinding.FragmentSplashBinding
import com.ibnu.jelajahin.utils.UiConstValue
import timber.log.Timber

class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!
    lateinit var pref: SharedPreferenceManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref = SharedPreferenceManager(requireContext())
        val token = pref.getToken ?: ""
        val isAlreadyIntroduced = pref.isAlreadyIntroduced
        Handler(Looper.getMainLooper()).postDelayed({
            when {
                !isAlreadyIntroduced -> {
                     findNavController().navigate(R.id.action_splashFragment_to_onBoardingFragment)
                }
                token.isBlank() -> {
                    findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                }
                else -> {
                    findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
                }
            }
        }, UiConstValue.LONG_ANIMATION_TIME)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}