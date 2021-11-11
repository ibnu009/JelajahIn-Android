package com.ibnu.jelajahin.ui.user.daftar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.ibnu.jelajahin.databinding.RegisterFragmentBinding

class RegisterFragment : Fragment() {

    private val viewModel: RegisterViewModel by viewModels()

    private var _binding: RegisterFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = RegisterFragmentBinding.inflate(inflater, container, false)
        return _binding?.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}