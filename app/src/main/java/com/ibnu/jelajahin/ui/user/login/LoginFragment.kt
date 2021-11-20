package com.ibnu.jelajahin.ui.user.login

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ibnu.jelajahin.R
import com.ibnu.jelajahin.core.data.remote.network.ApiResponse
import com.ibnu.jelajahin.core.data.remote.request.LoginBody
import com.ibnu.jelajahin.core.utils.JelajahinConstValues
import com.ibnu.jelajahin.core.utils.SharedPreferenceManager
import com.ibnu.jelajahin.databinding.LoginFragmentBinding
import com.ibnu.jelajahin.core.extention.isEmailValid
import com.ibnu.jelajahin.core.extention.popTap
import com.ibnu.jelajahin.core.extention.sha256
import com.ibnu.jelajahin.core.extention.showOKDialog
import com.ibnu.jelajahin.utils.UiConstValue
import com.ibnu.jelajahin.utils.UiConstValue.ERROR_TITLE
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()

    private var _binding: LoginFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var pref: SharedPreferenceManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LoginFragmentBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref = SharedPreferenceManager(requireContext())

        binding.btnLogin.setOnClickListener {
            it.popTap()
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()

            Handler(Looper.getMainLooper()).postDelayed({
                when {
                    email.isBlank() -> binding.edtEmail.error = "Email tidak boleh kosong!"
                    !email.isEmailValid() -> binding.edtEmail.error = "Email tidak valid!"
                    password.isBlank() -> binding.edtPassword.error = "Password tidak boleh kosong!"
                    else -> {
                        val request = LoginBody(
                            email, password.sha256()
                        )
                        loginUser(request)
                    }
                }
            }, UiConstValue.FAST_ANIMATION_TIME)
        }

        binding.btnRegister.setOnClickListener {
            it.popTap()
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }, UiConstValue.FAST_ANIMATION_TIME)
        }
    }

    private fun loginUser(request: LoginBody) {
        viewModel.login(request).observe(viewLifecycleOwner, { response ->
            when (response) {
                is ApiResponse.Loading -> {
                    Timber.d("Loading")
                    showLoading(true)
                }
                is ApiResponse.Error -> {
                    Timber.d("Error ${response.errorMessage}")
                    showLoading(false)
                    requireContext().showOKDialog(ERROR_TITLE, response.errorMessage)
                }
                is ApiResponse.Success -> {
                    Timber.d("token is ${response.data}")
                    showLoading(false)
                    pref.setStringPreference(JelajahinConstValues.KEY_TOKEN, response.data)
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                }
                else -> {
                    Timber.d("Unknown Error")
                }
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.bgDim.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.edtEmail.isClickable = !isLoading
        binding.edtPassword.isClickable = !isLoading
        binding.btnLogin.isClickable = !isLoading
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}