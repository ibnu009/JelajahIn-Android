package com.ibnu.jelajahin.ui.profile.history

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ibnu.jelajahin.core.data.model.PointHistory
import com.ibnu.jelajahin.core.data.remote.network.ApiResponse
import com.ibnu.jelajahin.core.ui.adapter.HistoryPointAdapter
import com.ibnu.jelajahin.databinding.FragmentPointHistoryBinding
import com.ibnu.jelajahin.extention.popTap
import com.ibnu.jelajahin.ui.profile.ProfileViewModel
import com.ibnu.jelajahin.utils.UiConstValue.FAST_ANIMATION_TIME
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class PointHistoryFragment : Fragment() {


    private val viewModel: ProfileViewModel by viewModels()

    private var _binding: FragmentPointHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var mAdapter: HistoryPointAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPointHistoryBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.appBar.imgBack.setOnClickListener {
            it.popTap()
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().popBackStack()
            }, FAST_ANIMATION_TIME)
        }
        binding.appBar.tvToolbarTitle.text = "History Point"

        initiateRecyclerview()
        lifecycleScope.launch {
            viewModel.getUserPointHistory().collect { points ->
               mAdapter.submitData(points)
            }
        }
    }

    private fun initiateRecyclerview() {
        mAdapter = HistoryPointAdapter()
        mAdapter.addLoadStateListener { loadState ->
            if (loadState.append.endOfPaginationReached) {
                if (mAdapter.itemCount < 1) {
                    Timber.d("Empty!")
                    binding.imgNoData.visibility = View.VISIBLE
                    binding.tvNoData.visibility = View.VISIBLE
                } else {
                    Timber.d("Ada isinya!")
                    binding.imgNoData.visibility = View.GONE
                    binding.tvNoData.visibility = View.GONE
                }
            }
            when(loadState.refresh){
                is LoadState.Loading -> {
                    showLoading(true)
                }
                is LoadState.NotLoading -> {
                    showLoading(false)
                }
                else -> showLoading(false)
            }
        }
        binding.rvPoint.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = mAdapter
            addItemDecoration(
                DividerItemDecoration(
                    binding.rvPoint.context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}