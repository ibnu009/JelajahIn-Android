package com.ibnu.jelajahin.ui.event.attend

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ibnu.jelajahin.R
import com.ibnu.jelajahin.core.data.model.Event
import com.ibnu.jelajahin.core.extention.popTap
import com.ibnu.jelajahin.databinding.FragmentSuccessAttendBinding
import com.ibnu.jelajahin.utils.UiConstValue
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SuccessAttendFragment : Fragment() {

    private var _binding: FragmentSuccessAttendBinding? = null
    private val binding get() = _binding!!

    private var event: Event? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSuccessAttendBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val safeArgs = arguments?.let { SuccessAttendFragmentArgs.fromBundle(it) }
        event = safeArgs?.event

        binding.cvReward.tvRewardPoint.text = context?.resources?.getString(R.string.points, event?.pointReward.toString())
        binding.cvReward.tvRewardXp.text = context?.resources?.getString(R.string.xp, event?.xpReward.toString())
        binding.btnBackToProfile.setOnClickListener {
            it.popTap()
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigate(R.id.action_successAttendFragment_to_profileFragment)
            }, UiConstValue.FAST_ANIMATION_TIME)
        }
        binding.btnBackToEvent.setOnClickListener {
            it.popTap()
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigate(R.id.action_successAttendFragment_to_eventFragment)
            }, UiConstValue.FAST_ANIMATION_TIME)
        }

    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}