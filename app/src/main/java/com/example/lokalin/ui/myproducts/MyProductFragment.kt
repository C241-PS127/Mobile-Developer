package com.example.lokalin.ui.myproducts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lokalin.ViewModelFactory
import com.example.lokalin.adapter.MyProductAdapter
import com.example.lokalin.databinding.FragmentMyProductBinding
import com.example.lokalin.ui.profile.ProfileViewModel
import com.example.lokalin.ui.wishlist.WishlistViewModel

class MyProductFragment : Fragment() {
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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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

        loading()
    }


    private fun setupAction() {
        val adapter = MyProductAdapter(myProductsViewModel)
        binding.recMyproducts.adapter = adapter
        val layoutManager = LinearLayoutManager(requireContext())
        binding.recMyproducts.layoutManager = layoutManager

        myProductsViewModel.products.observe(viewLifecycleOwner) { product ->
            if (product?.isNotEmpty() == true) {
                adapter.submitList(product.reversed())
            }
        }
    }

    private fun profile(token: String) {
        profileViewModel.getProfile(token)
        profileViewModel.profile.observe(viewLifecycleOwner, Observer {
            brandName = it[0].fullName.toString()
            myProductsViewModel.getProductsByBrand(brandName)
            setupAction()
        })
    }

    private fun loading() {
        myProductsViewModel.loading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE

            } else {
                binding.progressBar.visibility = View.GONE

            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}