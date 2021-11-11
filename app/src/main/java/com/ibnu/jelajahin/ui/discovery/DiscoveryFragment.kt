package com.ibnu.jelajahin.ui.discovery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.ibnu.jelajahin.databinding.DiscoveryFragmentBinding

class DiscoveryFragment : Fragment() {

    private val viewModel: DiscoveryViewModel by viewModels();

    private var _binding: DiscoveryFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DiscoveryFragmentBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}