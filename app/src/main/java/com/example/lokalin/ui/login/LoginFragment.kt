package com.example.lokalin.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.api.ApiConfig
import com.example.di.Injection
import com.example.lokalin.R
import com.example.lokalin.ViewModelFactory
import com.example.lokalin.databinding.FragmentLoginBinding
import com.example.lokalin.databinding.FragmentProfileBinding
import com.example.lokalin.ui.home.HomeViewModel
import com.example.lokalin.ui.profile.ProfileViewModel
import com.example.storyapp.data.pref.UserModel
import com.example.utils.ResultState

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupView()

        return root
    }

    private fun setupView() {
        binding.imgBack.setOnClickListener() {
            findNavController().navigateUp()
        }

        binding.tvSignupbtn.setOnClickListener() {
            it.findNavController().navigate(R.id.signUpFragment2)
        }

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val passResult = binding.passwordEditText.text.toString()


            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.emailEditText.error = "Email tidak valid"
                binding.emailEditText.requestFocus()
                return@setOnClickListener
            }

            if (passResult.isEmpty() || passResult.length < 6) {
                binding.passwordEditText.error = "Password harus memiliki setidaknya 6 karakter"
                binding.passwordEditText.requestFocus()
                return@setOnClickListener
            }

            viewModel.loginUser(email, passResult)
        }

        viewModel.users.observe(viewLifecycleOwner) { resultState ->
            when (resultState) {
                is ResultState.Loading -> {
                    showLoading(true)
                }

                is ResultState.Success -> {
                    val token = resultState.data.token
                    viewModel.saveSession(UserModel(token, true))
                    val userRepository = Injection.provideRepository(requireContext())
                    userRepository.updateApiService(ApiConfig.getApiService(token))
                    AlertDialog.Builder(requireActivity()).apply {
                        setTitle("Login Berhasil!")
                        setMessage("Lanjut ke halaman utama!")
                        setPositiveButton("Lanjut") { _, _ ->
                            findNavController().navigate(R.id.navigation_home)
                            findNavController().popBackStack(R.id.loginFragment, true)
                        }
                        create().show()
                    }
                    showLoading(false)
                }

                is ResultState.Error -> {
                    showToast(resultState.error)
                    val errorMessage = "Cek kembali data yang dimasukkan"
                    AlertDialog.Builder(requireActivity()).apply {
                        setTitle("Login Gagal")
                        setMessage(errorMessage)
                        setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                        create().show()
                    }
                    showLoading(false)
                }

                else -> {

                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}