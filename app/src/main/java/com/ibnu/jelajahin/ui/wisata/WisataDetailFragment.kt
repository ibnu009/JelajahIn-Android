package com.ibnu.jelajahin.ui.wisata

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.ibnu.jelajahin.R
import com.ibnu.jelajahin.databinding.WisataFragmentBinding

class WisataDetailFragment : Fragment() {

    private val viewModel: WisataViewModel by viewModels()

    private var _binding: WisataFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = WisataFragmentBinding.inflate(inflater, container, false)
        return _binding?.root    }

}