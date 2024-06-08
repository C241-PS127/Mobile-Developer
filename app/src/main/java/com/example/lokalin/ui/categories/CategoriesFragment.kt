package com.example.lokalin.ui.categories

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
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.lokalin.R
import com.example.lokalin.ViewModelFactory
import com.example.lokalin.databinding.FragmentCategoriesBinding
import com.example.lokalin.ui.cart.CartViewModel
import com.example.lokalin.ui.home.CategoryAdapter
import com.example.lokalin.ui.home.ExploreAdapter
import com.example.lokalin.ui.home.HomeFragmentDirections
import com.example.lokalin.ui.home.HomeViewModel
import com.example.response.CategoryResponseItem
import com.example.utils.ResultState

class CategoriesFragment : Fragment() {

    private var _binding: FragmentCategoriesBinding? = null
    private val viewModel by viewModels<CategoriesViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupAction()
        return root
    }

    private fun setupAction() {

        val adapter = CategoryAdapter()
        binding.recyclerView.adapter = adapter

        viewModel.categories.observe(viewLifecycleOwner) { stories ->
            adapter.submitList(stories)
        }

        viewModel.loading.observe(viewLifecycleOwner) { hasil ->
           // showLoading(hasil)
            binding.proggressbarViewpager.visibility = if (hasil) View.VISIBLE else View.GONE
        }

    }

    private fun showLoading(isLoading: Boolean) {
        binding.proggressbarViewpager.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}