package com.ibnu.jelajahin.ui.user.daftar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.ibnu.jelajahin.R
import com.ibnu.jelajahin.core.data.remote.network.ApiResponse
import com.ibnu.jelajahin.core.data.remote.request.RegisterBody
import com.ibnu.jelajahin.databinding.RegisterFragmentBinding
import com.ibnu.jelajahin.core.extention.isEmailValid
import com.ibnu.jelajahin.core.extention.sha256
import com.ibnu.jelajahin.core.extention.showOKDialog
import com.ibnu.jelajahin.utils.UiConstValue.ERROR_TITLE
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private val viewModel: RegisterViewModel by viewModels()

    private var _binding: RegisterFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = RegisterFragmentBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener {
            val fullName = binding.edtFullname.text.toString()
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()
            val confirmPassword = binding.edtConfirmPassword.text.toString()

            when {
                fullName.isBlank() -> binding.edtFullname.error = "Nama Tidak boleh kosong!"
                email.isBlank() -> binding.edtEmail.error = "Email tidak boleh kosong!"
                !email.isEmailValid() -> binding.edtEmail.error = "Email tidak valid!"
                password.isBlank() -> binding.edtPassword.error = "Password tidak boleh kosong!"
                password != confirmPassword -> binding.edtPassword.error = "Password tidak cocok!"
                else -> {
                    val request = RegisterBody(fullName, email, password.sha256())
                    registerUser(request)
                }
            }
        }
    }

    private fun registerUser(registerBody: RegisterBody) {
        viewModel.register(registerBody).observe(viewLifecycleOwner, { response ->
            when (response) {
                is ApiResponse.Loading -> {
                    Timber.d("Loading")
                    showLoading(true)
                }
                is ApiResponse.Error -> {
                    Timber.d("Error ${response.errorMessage}")
                    showLoading(false)
                    requireContext().showOKDialog(ERROR_TITLE,response.errorMessage)
                }
                is ApiResponse.Success -> {
                    showLoading(false)
                    showSuccessDialog()
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
        binding.edtFullname.isClickable = !isLoading
        binding.edtConfirmPassword.isClickable = !isLoading
        binding.btnRegister.isClickable = !isLoading
        binding.edtEmail.isEnabled = !isLoading
        binding.edtConfirmPassword.isEnabled = !isLoading
        binding.edtFullname.isEnabled = !isLoading
        binding.edtPassword.isEnabled = !isLoading
    }

    private fun showSuccessDialog(){
        AlertDialog.Builder(requireContext()).apply {
            setTitle("Berhasil Daftar!")
            setMessage("Kamu sekarang resmi menjadi keluarga JelajahIn, silahkan login dengan akun kamu")
            setPositiveButton("Siap") { p0, _ ->
                p0.dismiss()
                view?.findNavController()?.navigate(R.id.action_registerFragment_to_loginFragment)
            }
        }.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}