package com.ibnu.jelajahin.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.ibnu.jelajahin.R
import com.ibnu.jelajahin.core.utils.JelajahinConstValues
import com.ibnu.jelajahin.core.utils.SharedPreferenceManager
import com.ibnu.jelajahin.databinding.FragmentOnBoardingForthkBinding

class OnBoardingForthFragment : Fragment() {

    private var _binding: FragmentOnBoardingForthkBinding? = null
    private val binding get() = _binding!!

    lateinit var pref: SharedPreferenceManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOnBoardingForthkBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pref = SharedPreferenceManager(requireContext())
        val pager = activity?.findViewById<ViewPager2>(R.id.onboardingViewPager)

        binding.btnStart.setOnClickListener {
            try {
                pref.setBooleanPreference(JelajahinConstValues.KEY_IS_ALREADY_INTRODUCED, true)
            } finally {
                findNavController().navigate(R.id.action_onBoardingFragment_to_loginFragment)
            }
        }

        binding.circle1.setOnClickListener {
            pager?.currentItem = 0
        }
        binding.circle2.setOnClickListener {
            pager?.currentItem = 1
        }
        binding.circle3.setOnClickListener {
            pager?.currentItem = 2
        }
        binding.circle4.setOnClickListener {
            pager?.currentItem = 3
        }

    }

}