package com.ibnu.jelajahin.ui.penginapan

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.ibnu.jelajahin.R
import com.ibnu.jelajahin.core.extention.popTap
import com.ibnu.jelajahin.core.ui.adapter.PenginapanAdapter
import com.ibnu.jelajahin.core.ui.adapter.handler.RecyclerviewItemClickHandler
import com.ibnu.jelajahin.databinding.PenginapanFragmentBinding
import com.ibnu.jelajahin.utils.UiConstValue
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class PenginapanFragment : Fragment(), RecyclerviewItemClickHandler {

    private val viewModel: PenginapanViewModel by viewModels()

    private var _binding: PenginapanFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: PenginapanAdapter
    private var isSearching = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PenginapanFragmentBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initiateAdapter()
        initiateAppbar("Bondowoso")
        initiateData("")

        binding.svPenginapan.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                initiateData(query ?: "")
                isSearching = true
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText?.isBlank() == true && isSearching) {
                    initiateData("")
                    isSearching = false
                }
                return false
            }
        })
        binding.btnMap.setOnClickListener {
            it.popTap()
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigate(R.id.action_restaurantFragment_to_restaurantMapFragment)
            }, UiConstValue.FAST_ANIMATION_TIME)
        }

    }

    private fun initiateData(searchQuery: String) {
        lifecycleScope.launch {
            viewModel.getListPenginapan(0, searchQuery = searchQuery).collect { restaurant ->
                adapter.submitData(restaurant)
            }
        }
    }

    private fun initiateAppbar(cityName: String) {
        binding.appBar.tvToolbarTitle.text = "Penginapan di $cityName"
        binding.appBar.imgBack.setOnClickListener {
            it.popTap()
            findNavController().popBackStack()
        }
    }

    private fun initiateAdapter() {
        adapter = PenginapanAdapter(this)
        binding.rvPenginapan.adapter = this.adapter
        binding.rvPenginapan.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter.addLoadStateListener { loadState ->
            if (loadState.append.endOfPaginationReached) {
                if (adapter.itemCount < 1) {
                    Timber.d("Empty!")
                } else {
                    Timber.d("Ada isinya!")
                }
            }
            when (loadState.refresh) {
                is LoadState.Loading -> {
                    showLoading(true)
                }
                is LoadState.NotLoading -> {
                    showLoading(false)
                    binding.rvPenginapan.scheduleLayoutAnimation()
                }
                else -> showLoading(false)
            }
        }
        binding.rvPenginapan.scheduleLayoutAnimation()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.shimmeringPenginapanList.startShimmer()
            binding.shimmeringPenginapanList.showShimmer(true)
            binding.shimmeringPenginapanList.visibility = View.VISIBLE
            binding.containerPenginapanList.visibility = View.GONE
        } else {
            binding.shimmeringPenginapanList.startShimmer()
            binding.shimmeringPenginapanList.showShimmer(true)
            binding.shimmeringPenginapanList.visibility = View.GONE
            binding.containerPenginapanList.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onItemClicked(uuid: String) {
//        val action = PenginapanFragmentDirections.actionPenginapanFragmentToPenginapanDetailFragment(uuid)
//        findNavController().navigate(action)
    }
}