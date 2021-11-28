package com.ibnu.jelajahin.ui.wisata

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.ibnu.jelajahin.core.extention.popTap
import com.ibnu.jelajahin.core.ui.adapter.RecyclerviewItemClickHandler
import com.ibnu.jelajahin.core.ui.adapter.WisataAdapter
import com.ibnu.jelajahin.databinding.WisataFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class WisataFragment : Fragment(), RecyclerviewItemClickHandler {

    private val viewModel: WisataViewModel by viewModels()

    private var _binding: WisataFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: WisataAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = WisataFragmentBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initiateAppbar("Bondowoso")
        initiateAdapter()
        lifecycleScope.launch {
            viewModel.getListWisata(1,1).collect { events ->
                adapter.submitData(events)
            }
        }

        binding.btnMap.setOnClickListener {
            it.popTap()
        }
    }

    private fun initiateAppbar(cityName: String){
        binding.appBar.tvToolbarTitle.text = "Wisata di $cityName"
        binding.appBar.imgBack.setOnClickListener {
            it.popTap()
            findNavController().popBackStack()
        }
    }

    private fun initiateAdapter() {
        adapter = WisataAdapter(this)
        binding.rvWisata.adapter = this.adapter
        binding.rvWisata.layoutManager =
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
                    binding.rvWisata.scheduleLayoutAnimation()
                }
                else -> showLoading(false)
            }
        }
        binding.rvWisata.scheduleLayoutAnimation()
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

    override fun onItemClicked(uuid: String) {
        val action = WisataFragmentDirections.actionWisataFragmentToWisataDetailFragment(uuid)
        findNavController().navigate(action)
    }


}