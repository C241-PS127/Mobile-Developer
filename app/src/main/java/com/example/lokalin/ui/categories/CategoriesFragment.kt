package com.example.lokalin.ui.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.lokalin.ViewModelFactory
import com.example.lokalin.databinding.FragmentCategoriesBinding
import com.example.lokalin.adapter.CategoryAdapter

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

        val adapter = CategoryAdapter { categoryName ->
            val action = CategoriesFragmentDirections.actionNavigationCategoriesToSearchFragment(categoryName)
            view?.findNavController()?.navigate(action)
        }
        binding.recyclerView.adapter = adapter

        viewModel.categories.observe(viewLifecycleOwner) { stories ->
            adapter.submitList(stories)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}