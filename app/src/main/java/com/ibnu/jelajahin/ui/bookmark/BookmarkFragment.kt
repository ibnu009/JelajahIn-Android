package com.ibnu.jelajahin.ui.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.ibnu.jelajahin.core.extention.showExitJelajahInDialog
import com.ibnu.jelajahin.core.utils.SharedPreferenceManager
import com.ibnu.jelajahin.databinding.BookmarkFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarkFragment : Fragment() {

    private var _binding: BookmarkFragmentBinding? = null
    private val binding get() = _binding!!

    lateinit var pref: SharedPreferenceManager
    private var token: String = ""
    private var email: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().showExitJelajahInDialog()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BookmarkFragmentBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pref = SharedPreferenceManager(requireContext())
        token = pref.getToken ?: ""
        email = pref.getEmail ?: ""

        val bookmarkViewPager = BookmarkViewPagerAdapter(requireActivity(), email)
        binding.bookmarkViewPager.adapter = bookmarkViewPager

        TabLayoutMediator(
            binding.bookmarkTabLayout, binding.bookmarkViewPager
        ) { tab, position ->
            tab.text = titles[position]
        }.attach()

    }

    private val titles = listOf(
        "Wisata",
        "Penginapan",
        "Restaurant"
    )

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}