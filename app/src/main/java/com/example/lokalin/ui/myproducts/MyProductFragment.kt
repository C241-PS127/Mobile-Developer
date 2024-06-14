package com.example.lokalin.ui.myproducts

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lokalin.ViewModelFactory
import com.example.lokalin.databinding.FragmentAddProductBinding
import com.example.lokalin.databinding.FragmentMyProductBinding
import com.example.lokalin.ui.cart.CartViewModel
import com.example.lokalin.ui.home.WishlistAdapter
import com.example.lokalin.ui.profile.ProfileViewModel
import com.example.lokalin.ui.wishlist.WishlistViewModel

class MyProductsFragment : Fragment() {
    private var _binding: FragmentMyProductBinding? = null
    private val binding get() = _binding!!

    private lateinit var brandName: String

    private val myProductsViewModel by viewModels<MyProductViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private val wishlistViewModel by viewModels<WishlistViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private val profileViewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyProductBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupView()

        return root
    }

    private fun setupView() {
        wishlistViewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (user.isLogin) {
                profile(user.token)
            }
        }
    }


    private fun setupAction() {
        val adapter = MyProductAdapter()
        binding.recMyproducts.adapter = adapter
        val layoutManager = LinearLayoutManager(requireContext())
        binding.recMyproducts.layoutManager = layoutManager

        myProductsViewModel.products.observe(viewLifecycleOwner) { product ->
            if (product?.isNotEmpty() == true) {
                adapter.submitList(product)
            }
            Log.d("TAG", "ISI DARI MY PRODUCT ${product}")
        }
    }

    private fun profile(token: String) {
        profileViewModel.getProfile(token)
        profileViewModel.profile.observe(viewLifecycleOwner, Observer {
            brandName = it[0].fullName.toString()
            myProductsViewModel.getProductsByBrand(brandName)
            setupAction()
            Log.d("TAG", "ISI DARI BRAND NAME ${brandName}")
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}