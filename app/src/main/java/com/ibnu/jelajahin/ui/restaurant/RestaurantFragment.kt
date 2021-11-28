package com.ibnu.jelajahin.ui.restaurant

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.ibnu.jelajahin.databinding.RestaurantFragmentBinding

class RestaurantFragment : Fragment() {

    private val viewModel: RestaurantViewModel by viewModels()

    private var _binding: RestaurantFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = RestaurantFragmentBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}