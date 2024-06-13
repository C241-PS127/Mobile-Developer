package com.example.lokalin.ui.addproduct

import android.R
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.lokalin.ViewModelFactory
import com.example.lokalin.databinding.FragmentAddProductBinding
import com.example.lokalin.ui.categories.CategoriesViewModel

class AddProductFragment : Fragment() {
    private var _binding: FragmentAddProductBinding? = null
    private val binding get() = _binding!!

    private val categoryViewModel by viewModels<CategoriesViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private lateinit var categoryAdapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddProductBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupView()

        return root
    }

    private fun setupView() {
        categoryAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            mutableListOf()
        )
        binding.categoryAutoCompleteTextView.setAdapter(categoryAdapter)

        categoryViewModel.categories.observe(viewLifecycleOwner) { categories ->
            categories?.let {
                val categoryNames = it.map { category -> category.categoryName }
                categoryAdapter.clear()
                categoryAdapter.addAll(categoryNames)
                categoryAdapter.notifyDataSetChanged()
                Log.d("TAG", "Isi dari categori ${categories}")
            }
        }

        binding.categoryAutoCompleteTextView.setOnClickListener {
            Log.d("TAG", "AutoCompleteTextView clicked")
            binding.categoryAutoCompleteTextView.showDropDown()
        }

        binding.categoryAutoCompleteTextView.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                binding.categoryAutoCompleteTextView.showDropDown()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
