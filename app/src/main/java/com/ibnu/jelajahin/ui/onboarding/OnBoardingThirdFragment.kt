package com.ibnu.jelajahin.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ibnu.jelajahin.R
import com.ibnu.jelajahin.core.utils.JelajahinConstValues
import com.ibnu.jelajahin.core.utils.SharedPreferenceManager
import com.ibnu.jelajahin.databinding.FragmentOnBoardingSecondBinding
import com.ibnu.jelajahin.databinding.FragmentOnBoardingThirdBinding

class OnBoardingThirdFragment : Fragment() {

    private var _binding: FragmentOnBoardingThirdBinding? = null
    private val binding get() = _binding!!

    lateinit var pref: SharedPreferenceManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOnBoardingThirdBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref = SharedPreferenceManager(requireContext())

        binding.btnStart.setOnClickListener {
            try {
                pref.setBooleanPreference(JelajahinConstValues.KEY_IS_ALREADY_INTRODUCED, true)
            } finally {
                findNavController().navigate(R.id.action_onBoardingThirdFragment_to_loginFragment)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}