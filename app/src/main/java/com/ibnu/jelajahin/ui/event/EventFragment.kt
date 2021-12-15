package com.ibnu.jelajahin.ui.event

import android.os.Bundle
import android.util.Log
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
import com.ibnu.jelajahin.core.ui.adapter.EventAdapter
import com.ibnu.jelajahin.core.ui.adapter.handler.RecyclerviewItemClickHandler
import com.ibnu.jelajahin.databinding.EventFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class EventFragment : Fragment(), RecyclerviewItemClickHandler {

    private val viewModel: EventViewModel by viewModels()

    private var _binding: EventFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: EventAdapter
    private var isSearching = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = EventFragmentBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initiateAdapter()
        initiateData(null)

        binding.svEvent.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Timber.d("search submit called")
                initiateData(query ?: "")
                isSearching = true
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText?.isBlank() == true && isSearching) {
                    Timber.d("query changed called")
                    initiateData("")
                    isSearching = false
                }
                return false
            }
        })
    }

    private fun initiateData(searchQuery: String?) {
        lifecycleScope.launch {
            Timber.d("launch search")
            viewModel.getEvents(15, 229, searchQuery ?: "").collect { events ->
                adapter.submitData(events)
            }
        }
    }

    private fun initiateAdapter() {
        adapter = EventAdapter(this)
        binding.rvEvent.adapter = this.adapter
        binding.rvEvent.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter.addLoadStateListener { loadState ->
            if (loadState.append.endOfPaginationReached) {
                if (adapter.itemCount < 1) {
                    Timber.d("Empty!")
                } else {
                    Timber.d("Ada isinya!")
                }
            }
            when(loadState.refresh){
                is LoadState.Loading -> {
                    showLoading(true)
                }
                is LoadState.NotLoading -> {
                    showLoading(false)
                    binding.rvEvent.scheduleLayoutAnimation()
                }
                else -> showLoading(false)
            }
        }
        binding.rvEvent.scheduleLayoutAnimation()
    }

    private fun showLoading(isLoading: Boolean){
        if (isLoading){
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClicked(uuid: String) {
        val action = EventFragmentDirections.actionEventFragmentToEventDetailFragment(uuid)
        findNavController().navigate(action)
    }
}