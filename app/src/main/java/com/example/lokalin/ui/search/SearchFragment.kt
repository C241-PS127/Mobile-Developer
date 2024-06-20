package com.example.lokalin.ui.search

import android.app.SearchManager
import android.database.Cursor
import android.database.MatrixCursor
import android.os.Bundle
import android.provider.BaseColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.RadioButton
import androidx.appcompat.widget.SearchView
import androidx.cursoradapter.widget.CursorAdapter
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lokalin.R
import com.example.lokalin.ViewModelFactory
import com.example.lokalin.adapter.BrandAdapter
import com.example.lokalin.adapter.RecommendationAdapterSearch
import com.example.lokalin.databinding.FragmentSearchBinding
import com.example.lokalin.ui.home.HomeFragmentDirections
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
        val from = arrayOf(SearchManager.SUGGEST_COLUMN_TEXT_1)
        val to = intArrayOf(R.id.searchItemID)
        val cursorAdapter = SimpleCursorAdapter(
            requireContext(),
            R.layout.suggestion_item_layout,
            null,
            from,
            to,
            CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        )

        binding.searchview.suggestionsAdapter = cursorAdapter

        binding.searchview.setOnSuggestionListener(object : SearchView.OnSuggestionListener {
            override fun onSuggestionSelect(position: Int): Boolean {
                return true
            }

            override fun onSuggestionClick(position: Int): Boolean {
                val cursor = binding.searchview.suggestionsAdapter.getItem(position) as Cursor
                val columnIndex = cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1)
                if (columnIndex >= 0) {
                    val suggestion = cursor.getString(columnIndex)
                    binding.searchview.setQuery(suggestion, true)
                }
                binding.searchview.setQuery("", false)
                return true
            }
        })

        binding.searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.getProductRecommendation(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val fullUrl = "https://nextwords-app-tjhaye3n5q-et.a.run.app/predict"
                if (newText != null) {
                    viewModel.predict(fullUrl, newText)
                }
                viewModel.prediction.observe(viewLifecycleOwner, Observer { predictedText ->
                    val cursor = getSuggestions(predictedText)
                    cursorAdapter.changeCursor(cursor)
                })
                return true
            }
        })
    }

    private fun getSuggestions(predictedText: String?): Cursor {
        val matrixCursor =
            MatrixCursor(arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1))
        if (predictedText != null) {
            val suggestions = listOf(predictedText)
            var id = 0
            for (suggestion in suggestions) {
                matrixCursor.addRow(arrayOf(id++, suggestion))
            }
        }
        return matrixCursor
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
