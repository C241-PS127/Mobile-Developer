package com.example.lokalin.ui.wishlist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lokalin.R
import com.example.lokalin.ViewModelFactory
import com.example.lokalin.databinding.FragmentProfileBinding
import com.example.lokalin.databinding.FragmentWishlistBinding
import com.example.lokalin.ui.cart.CartViewModel
import com.example.lokalin.ui.categories.CategoriesViewModel
import com.example.lokalin.ui.home.CategoryAdapter
import com.example.lokalin.ui.home.WishlistAdapter
import com.example.lokalin.ui.profile.ProfileViewModel

class WishlistFragment : Fragment() {

    private var _binding: FragmentWishlistBinding? = null

    private val viewModel by viewModels<WishlistViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private val cartViewModel by viewModels<CartViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWishlistBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupView()

        binding.swipeRefresh.setOnRefreshListener {
            setupView()
            binding.swipeRefresh.isRefreshing = false
        }

        return root
    }

    private fun setupAction(token: String) {
        val adapter = WishlistAdapter(viewModel, token)
        binding.recWishlist.adapter = adapter
        val layoutManager = LinearLayoutManager(requireContext())
        binding.recWishlist.layoutManager = layoutManager

        viewModel.wishlist.observe(viewLifecycleOwner) { wishlist ->
            if (wishlist?.isNotEmpty() == true) {
                adapter.submitList(wishlist)
            }
        }
    }

    private fun loading(){
        viewModel.loading.observe(viewLifecycleOwner, Observer { Loading ->
            if (Loading) {
                // Tampilkan indikator loading
                binding.progressBar.visibility = View.VISIBLE
            } else {
                // Sembunyikan indikator loading
                binding.progressBar.visibility = View.GONE
            }
        })
    }

    private fun setupView(){
        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (user.isLogin) {
                setupAction(user.token)
                viewModel.allWishlist(user.token)
            } else {
                Toast.makeText(requireContext(), "You are not logged in", Toast.LENGTH_SHORT).show()
            }
        }
        loading()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}