package com.example.lokalin.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.RadioButton
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lokalin.R
import com.example.lokalin.ViewModelFactory
import com.example.lokalin.adapter.BrandAdapter
import com.example.lokalin.adapter.RecommendationAdapterSearch
import com.example.lokalin.databinding.FragmentSearchBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val viewModel by viewModels<SearchViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private val binding get() = _binding!!
    lateinit var bottomSheetDialog: BottomSheetDialog
    lateinit var cbAZ: RadioButton
    lateinit var cbZA: RadioButton
    lateinit var cbTerdekat: RadioButton
    lateinit var cbTerjauh: RadioButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupView()

        binding.swipeRefreshLayout.setOnRefreshListener() {
            setupView()
            binding.swipeRefreshLayout.isRefreshing = false
        }

        return root
    }

    private fun setupSearchView() {
        binding.searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.getProductRecommendation(it)

                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun setFilterData() {
        binding.buttonFilter.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.filter_bottom, null)

            cbAZ = dialogView.findViewById(R.id.cbAZ)
            cbZA = dialogView.findViewById(R.id.cbZA)
            cbTerdekat = dialogView.findViewById(R.id.cbTerdekat)
            cbTerjauh = dialogView.findViewById(R.id.cbTerjauh)

            bottomSheetDialog = BottomSheetDialog(requireContext())
            bottomSheetDialog.setContentView(dialogView)
            bottomSheetDialog.show()

            cbAZ.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
                if (isChecked) {
                }
            }

            cbZA.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
                if (isChecked) {
                }
            }

            cbTerdekat.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
                if (isChecked) {
                }
            }

            cbTerjauh.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
                if (isChecked) {
                }
            }
        }
    }

    private fun rvBrand() {
        val adapter = BrandAdapter { brandName ->
            binding.searchview.setQuery(brandName, true)
        }

        binding.rvBrands.adapter = adapter
        binding.rvBrands.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        viewModel.brands.observe(viewLifecycleOwner) { brands ->
            adapter.submitList(brands)
        }
    }

    private fun recommendation() {

        val adapter2 = RecommendationAdapterSearch()
        binding.rvExplore.adapter = adapter2
        binding.rvExplore.layoutManager = GridLayoutManager(requireContext(), 2)

        viewModel.productRecommendation.observe(viewLifecycleOwner, Observer { products ->
            Log.d("SearchFragment", "Products: $products")
            adapter2.submitList(products)
        })
    }

    private fun loading() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.proggressbarSearch.visibility = View.GONE
            } else {
                binding.proggressbarSearch.visibility = View.VISIBLE

            }
        }
    }

    private fun setupView() {
        setFilterData()
        setupSearchView()
        rvBrand()
        recommendation()
        loading()

        val args: SearchFragmentArgs by navArgs()
        val query = args.query
        binding.searchview.setQuery(query, false)
        viewModel.getProductRecommendation(query)

        binding.imgBack.setOnClickListener {
            findNavController().navigateUp()
        }

    }
}
