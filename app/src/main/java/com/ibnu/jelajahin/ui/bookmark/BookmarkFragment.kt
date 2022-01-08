package com.ibnu.jelajahin.ui.bookmark

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.ibnu.jelajahin.R
import com.ibnu.jelajahin.core.extention.popTap
import com.ibnu.jelajahin.core.extention.showExitJelajahInDialog
import com.ibnu.jelajahin.core.utils.SharedPreferenceManager
import com.ibnu.jelajahin.databinding.BookmarkFragmentBinding
import com.ibnu.jelajahin.utils.UiConstValue
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

        if (token == "" && email == ""){
            initiateNotLoggedUser()
        } else {
            initiateLoggedUserView()
        }

    }

    private fun initiateLoggedUserView(){
        initiateAppBar()

        val bookmarkViewPager = BookmarkViewPagerAdapter(requireActivity(), email)
        binding.bookmarkViewPager.adapter = bookmarkViewPager

        TabLayoutMediator(
            binding.bookmarkTabLayout, binding.bookmarkViewPager
        ) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }

    private fun initiateNotLoggedUser() {
        binding.contentProfile.visibility = View.GONE
        binding.contentNotUser.visibility = View.VISIBLE

        binding.btnGoToLogin.setOnClickListener {
            it.popTap()
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigate(R.id.action_bookmarkFragment_to_loginFragment)
            }, UiConstValue.FAST_ANIMATION_TIME)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initiateAppBar() {
        binding.appBar.tvToolbarTitle.text = "Bookmark"
        binding.appBar.tvToolbarTitle.setTextColor(ContextCompat.getColor(
            requireContext(),
            R.color.white
        ))
        binding.appBar.root.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.green_button
            )
        )
        binding.appBar.imgBack.visibility = View.GONE
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