package com.ibnu.jelajahin.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.ibnu.jelajahin.R
import com.ibnu.jelajahin.core.extention.showExitJelajahInDialog
import com.ibnu.jelajahin.databinding.FragmentOnBoardingFirstBinding

class OnBoardingFirstFragment : Fragment() {

    private var _binding: FragmentOnBoardingFirstBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOnBoardingFirstBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pager = activity?.findViewById<ViewPager2>(R.id.onboardingViewPager)
        binding.btnNext.setOnClickListener {
            pager?.currentItem = 1
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}