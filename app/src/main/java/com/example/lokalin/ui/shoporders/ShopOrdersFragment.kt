package com.example.lokalin.ui.shoporders

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lokalin.ViewModelFactory
import com.example.lokalin.adapter.ShopOrdersAdapter
import com.example.lokalin.databinding.FragmentShopOrdersBinding

class ShopOrdersFragment : Fragment() {
    private var _binding: FragmentShopOrdersBinding? = null
    private val binding get() = _binding!!

    private val shopOrdersViewModel by viewModels<ShopOrdersViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShopOrdersBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupAction()
        loading()

        return root
    }

    private fun setupAction() {
        val adapter = ShopOrdersAdapter(shopOrdersViewModel)
        binding.recShoporders.adapter = adapter
        val layoutManager = LinearLayoutManager(requireContext())
        binding.recShoporders.layoutManager = layoutManager

        shopOrdersViewModel.shoporders.observe(viewLifecycleOwner) { product ->
            if (product?.isNotEmpty() == true) {
                adapter.submitList(product)
            }
        }
    }

    private fun loading(){
        shopOrdersViewModel.loading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE

            } else {
                binding.progressBar.visibility = View.GONE

            }
        })
    }

}