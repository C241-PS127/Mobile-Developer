package com.example.lokalin.ui.signup

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.lokalin.ViewModelFactory
import com.example.lokalin.databinding.FragmentSignUpBinding
import com.example.utils.ResultState

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val viewModel by viewModels<SignUpViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupView()

        return root
    }

    private fun setupView() {
        binding.imgBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.signupbtn.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val address = binding.addressEditText.text.toString()
            val phoneNumber = binding.phoneEditText.text.toString()
            val passResult = binding.passwordEditText.text.toString()
            val name = binding.nameEditText.text.toString()

            if (name.isEmpty()) {
                binding.nameEditText.error = "Nama harus diisi"
                binding.nameEditText.requestFocus()
                return@setOnClickListener
            }

            if (address.isEmpty()) {
                binding.addressEditText.error = "Alamat harus diisi"
                binding.addressEditText.requestFocus()
                return@setOnClickListener
            }

            if (phoneNumber.isEmpty()) {
                binding.phoneEditText.error = "Nomor telpon harus diisi"
                binding.phoneEditText.requestFocus()
                return@setOnClickListener
            }

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

            viewModel.registerUser(name, address, phoneNumber, email, passResult)
        }

        viewModel.users.observe(viewLifecycleOwner) { resultState ->
            when (resultState) {
                is ResultState.Loading -> {
                    showLoading(true)
                }

                is ResultState.Success -> {
                    AlertDialog.Builder(requireActivity()).apply {
                        setTitle("Sign Up Berhasil!")
                        setMessage("Lanjut ke halaman login!")
                        setPositiveButton("Lanjut") { _, _ ->
                            findNavController().navigateUp()
                        }
                        create().show()
                    }
                    showLoading(false)
                }

                is ResultState.Error -> {
                    val errorMessage = "Cek kembali data yang dimasukkan"
                    AlertDialog.Builder(requireActivity()).apply {
                        setTitle("Sign Up Gagal")
                        setMessage(errorMessage)
                        setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                        create().show()
                    }
                    showLoading(false)
                }

                else -> {
                    // Handle loading state if needed
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


