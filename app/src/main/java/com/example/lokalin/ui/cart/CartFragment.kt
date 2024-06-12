package com.example.lokalin.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lokalin.R
import com.example.lokalin.ViewModelFactory
import com.example.lokalin.databinding.FragmentCartBinding
import com.example.lokalin.ui.cart.CartViewModel
import com.example.lokalin.ui.wishlist.WishlistViewModel

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null

    private val viewModel by viewModels<CartViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        val root: View = binding.root



        binding.swipeRefreshLayout.setOnRefreshListener {
            // Lakukan tindakan untuk memuat ulang data
            // Misalnya, muat ulang data dari server atau database lokal
            loadData()
        }

        binding.historyImg.setOnClickListener() {
            it.findNavController().navigate(R.id.historyFragment)
        }

        loadCart()

        return root
    }

    private fun setupAction(token: String) {
        val adapter = CartAdapter(viewModel, token)
        binding.recCart.adapter = adapter
        val layoutManager = LinearLayoutManager(requireContext())
        binding.recCart.layoutManager = layoutManager

        viewModel.cart.observe(viewLifecycleOwner) { cart ->
            Log.d("TAG", "Hasil Cartku ${cart}")
            if (cart?.isNotEmpty() == true) {
                adapter.submitList(cart)
            }
        }
    }

    private fun loadCart(){
        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (user.isLogin) {
                setupAction(user.token)
                viewModel.allCart(user.token)
            } else {
                Toast.makeText(requireContext(), "You are not logged in", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun loadData() {
        loadCart()
        binding.swipeRefreshLayout.isRefreshing = false
    }
}