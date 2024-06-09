package com.example.lokalin.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        login()

        binding.imgBack.setOnClickListener() {
            findNavController().navigateUp()
        }

        binding.tvSignupbtn.setOnClickListener() {
            it.findNavController().navigate(R.id.signUpFragment2)
        }

        binding.emailEditText.addTextChangedListener {
            setMyButtonEnable()
        }
        binding.passwordEditText.addTextChangedListener {
            setMyButtonEnable()
        }

        return root
    }

    private fun login() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val passResult = binding.passwordEditText.text.toString()
            viewModel.loginUser(email = email, password = passResult)
        }

        viewModel.users.observe(requireActivity()) { result ->
            if (result != null) {
                when (result) {
                    is ResultState.Loading -> {
//                        showLoading(true)
                    }

                    is ResultState.Success -> {
                        val token = result.data.token
                        viewModel.saveSession(UserModel(token, true))
                        val userRepository = Injection.provideRepository(requireContext())
                        userRepository.updateApiService(ApiConfig.getApiService(token))
                        AlertDialog.Builder(requireActivity()).apply {
                            setTitle("Login Berhasil!")
                            setMessage("Lanjut jika ingin berbelanja")
                            setPositiveButton("Lanjut") { _, _ ->
                                findNavController().navigate(R.id.navigation_home)
                                findNavController().popBackStack(R.id.loginFragment, true)
//                                intent.flags =
//                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//                                startActivity(intent)
//                                finish()
                            }
                            create().show()
                        }
//                        showLoading(false)

                    }

                    is ResultState.Error -> {
//                        showToast(result.error)
//                        showLoading(false)

                    }
                }
            }
        }
    }

    private fun setMyButtonEnable() {
        val email = binding.emailEditText.text
        val passResult = binding.passwordEditText.text
        binding.loginButton.isEnabled = email != null && passResult != null && email.toString()
            .isNotEmpty() && passResult.toString().isNotEmpty()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}