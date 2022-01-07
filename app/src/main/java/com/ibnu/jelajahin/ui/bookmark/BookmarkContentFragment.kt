package com.ibnu.jelajahin.ui.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ibnu.jelajahin.core.data.local.room.query.TypeUtils.FAVORITE_PENGINAPAN
import com.ibnu.jelajahin.core.data.local.room.query.TypeUtils.FAVORITE_RESTAURANT
import com.ibnu.jelajahin.core.data.local.room.query.TypeUtils.FAVORITE_WISATA
import com.ibnu.jelajahin.core.ui.adapter.BookmarkAdapter
import com.ibnu.jelajahin.core.ui.adapter.handler.BookmarkItemHandler
import com.ibnu.jelajahin.databinding.FragmentBookmarkContentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarkContentFragment(private val type: String, private val email: String) : Fragment(),
    BookmarkItemHandler {

    private val viewModel: BookmarkViewModel by viewModels()

    private var _binding: FragmentBookmarkContentBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: BookmarkAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookmarkContentBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initiateAdapter()
        viewModel.getBookmarkedItem(type, email).observe(viewLifecycleOwner, {
            adapter.setData(it)
        })
    }

    private fun initiateAdapter() {
        adapter = BookmarkAdapter(this)
        binding.rvBookmark.adapter = adapter
        binding.rvBookmark.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    override fun onItemClicked(uuid: String, type: String) {
        when (type) {
            FAVORITE_WISATA -> {
                val action =
                    BookmarkFragmentDirections.actionBookmarkFragmentToWisataDetailFragment(uuid)
                findNavController().navigate(action)
            }
            FAVORITE_RESTAURANT -> {
                val action =
                    BookmarkFragmentDirections.actionBookmarkFragmentToRestaurantDetailFragment(uuid)
                findNavController().navigate(action)
            }
            FAVORITE_PENGINAPAN -> {
                val action =
                    BookmarkFragmentDirections.actionBookmarkFragmentToPenginapanDetailFragment(uuid)
                findNavController().navigate(action)
            }
        }
    }

    override fun onUnBookmarkClick(uuid: String) {
        viewModel.removeItemFromFavorite(uuid, email)
    }
}
