package com.ibnu.jelajahin.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ibnu.jelajahin.R
import com.ibnu.jelajahin.databinding.FragmentOnBoardingBinding
import com.ibnu.jelajahin.databinding.FragmentOnBoardingFirstBinding
import com.ibnu.jelajahin.ui.bookmark.BookmarkViewPagerAdapter

class OnBoardingFragment : Fragment() {

    private var _binding: FragmentOnBoardingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOnBoardingBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val onBoardingViewPager = OnBoardingViewPagerAdapter(requireActivity())
        binding.onboardingViewPager.adapter = onBoardingViewPager

    }

}