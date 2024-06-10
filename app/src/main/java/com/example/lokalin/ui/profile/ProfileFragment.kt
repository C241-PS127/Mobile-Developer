package com.example.lokalin.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.lokalin.R
import com.example.lokalin.ViewModelFactory
import com.example.lokalin.databinding.FragmentHomeBinding
import com.example.lokalin.databinding.FragmentProfileBinding
import com.example.lokalin.ui.categories.CategoriesViewModel
import com.example.lokalin.ui.home.HomeViewModel
import com.example.utils.ResultState

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val viewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (user.isLogin) {
                setupAction(user.token)
                binding.btnLogout.visibility = View.VISIBLE
                Log.d("Tag", "Token ${user.token}")
            }
        }

        return root
    }

    private fun setupAction(token: String) {
        viewModel.getProfile(token).observe(viewLifecycleOwner) {
            if (it != null) {
                when (it) {
                    is ResultState.Loading -> {
                        //showLoading(true)
                    }

                    is ResultState.Success -> {

                        binding.apply {
                            name.text = it.data[0].fullName
                            email.text = it.data[0].email
                            phone.text = it.data[0].phone
                            address.text = it.data[0].address
                        }
                    }

                    is ResultState.Error -> {
                        //showLoading(false)
                       // showToast(it.error)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}