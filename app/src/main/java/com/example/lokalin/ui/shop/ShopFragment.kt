package com.example.lokalin.ui.shop

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.lokalin.R
import com.example.lokalin.ViewModelFactory
import com.example.lokalin.databinding.FragmentProfileBinding
import com.example.lokalin.databinding.FragmentShopBinding
import com.example.lokalin.ui.profile.ProfileViewModel
import com.example.lokalin.ui.search.SearchViewModel

class ShopFragment : Fragment() {
    private var _binding: FragmentShopBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShopBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupView()
        setupAction()

        return root
    }


    private fun setupAction(){
        binding.apply {
            layAddproduct.setOnClickListener {
                findNavController().navigate(R.id.addProductFragment)
            }
            layMyproduct.setOnClickListener {
                findNavController().navigate(R.id.myProductFragment)
            }
        }

    }

    private fun setupView(){
        profileViewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (user.isLogin) {
                profile(user.token)
            }
        }
    }


    private fun profile(token:String){
        profileViewModel.getProfile(token)
        profileViewModel.profile.observe(viewLifecycleOwner, Observer {
            binding.apply {
                tvName.text = it[0].fullName
            }
        })
    }

}