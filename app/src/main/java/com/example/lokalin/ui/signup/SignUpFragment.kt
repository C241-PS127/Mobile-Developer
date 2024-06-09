package com.example.lokalin.ui.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.lokalin.R
import com.example.lokalin.ViewModelFactory
import com.example.lokalin.databinding.FragmentLoginBinding
import com.example.lokalin.databinding.FragmentSignUpBinding
import com.example.lokalin.ui.login.LoginViewModel

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

        binding.signupbtn.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val address = binding.addressEditText.text.toString()
            val phoneNumber = binding.phoneEditText.text.toString()

            val passResult = binding.passwordEditText.text.toString()
            val name = binding.nameEditText.text.toString()
            viewModel.registerUser(
                name = name,
                address = address,
                phone = phoneNumber,
                email = email,
                password = passResult
            )
            AlertDialog.Builder(requireActivity()).apply {
                setTitle("Sign Up Berhasil!")
                setMessage("Lanjut ke halaman login!")
                setPositiveButton("Lanjut") { _, _ ->
                    it.findNavController().navigateUp()
                }
                create().show()
            }
        }

        binding.emailEditText.addTextChangedListener {
            setMyButtonEnable()
        }
        binding.passwordEditText.addTextChangedListener {
            setMyButtonEnable()
        }
        binding.nameEditText.addTextChangedListener {
            setMyButtonEnable()
        }
        binding.phoneEditText.addTextChangedListener {
            setMyButtonEnable()
        }
        binding.addressEditText.addTextChangedListener {
            setMyButtonEnable()
        }

        return root
    }

    private fun setMyButtonEnable() {
        val email = binding.emailEditText.text
        val address = binding.addressEditText.text
        val phoneNumber = binding.phoneEditText.text
        val passResult = binding.passwordEditText.text
        val name = binding.nameEditText.text
        binding.signupbtn.isEnabled =
            email != null && passResult != null && name != null && address != null && phoneNumber != null && email.toString()
                .isNotEmpty() && address.toString().isNotEmpty() && phoneNumber.toString()
                .isNotEmpty() && passResult.toString().isNotEmpty() && name.toString().isNotEmpty()
    }
}