package com.ibnu.jelajahin.ui.restaurant

import android.os.Bundle
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
import com.ibnu.jelajahin.core.extention.popTap
import com.ibnu.jelajahin.core.ui.adapter.RestaurantAdapter
import com.ibnu.jelajahin.core.ui.adapter.handler.RecyclerviewItemClickHandler
import com.ibnu.jelajahin.databinding.RestaurantFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class RestaurantFragment : Fragment(), RecyclerviewItemClickHandler {

    private val viewModel: RestaurantViewModel by viewModels()

    private var _binding: RestaurantFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: RestaurantAdapter
    private var isSearching = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = RestaurantFragmentBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initiateAdapter()
        initiateAppbar("Bondowoso")
        initiateData("")

        binding.svRestaurant.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
    }

    private fun initiateData(searchQuery: String) {
        lifecycleScope.launch {
            viewModel.getListRestaurant(0, searchQuery = searchQuery).collect { restaurant ->
                adapter.submitData(restaurant)
            }
        }
    }

    private fun initiateAppbar(cityName: String) {
        binding.appBar.tvToolbarTitle.text = "Restaurant di $cityName"
        binding.appBar.imgBack.setOnClickListener {
            it.popTap()
            findNavController().popBackStack()
        }
    }

    private fun initiateAdapter() {
        adapter = RestaurantAdapter(this)
        binding.rvRestaurant.adapter = this.adapter
        binding.rvRestaurant.layoutManager =
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
                    binding.rvRestaurant.scheduleLayoutAnimation()
                }
                else -> showLoading(false)
            }
        }
        binding.rvRestaurant.scheduleLayoutAnimation()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.shimmeringEventList.startShimmer()
            binding.shimmeringEventList.showShimmer(true)
            binding.shimmeringEventList.visibility = View.VISIBLE
            binding.containerEventList.visibility = View.GONE
        } else {
            binding.shimmeringEventList.startShimmer()
            binding.shimmeringEventList.showShimmer(true)
            binding.shimmeringEventList.visibility = View.GONE
            binding.containerEventList.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onItemClicked(uuid: String) {
        TODO("Not yet implemented")
    }

}