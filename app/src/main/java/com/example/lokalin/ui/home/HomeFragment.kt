package com.example.lokalin.ui.home

import android.app.SearchManager
import android.content.Intent
import android.database.Cursor
import android.database.MatrixCursor
import android.os.Bundle
import android.provider.BaseColumns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.cursoradapter.widget.CursorAdapter
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.lokalin.R
import com.example.lokalin.ViewModelFactory
import com.example.lokalin.databinding.FragmentHomeBinding
import com.example.lokalin.ui.profile.RecommendationAdapter
import com.example.lokalin.ui.search.BrandAdapter
import com.example.response.SliderModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupView()

        binding.swipeRefreshLayout.setOnRefreshListener {
            setupView()
            binding.swipeRefreshLayout.isRefreshing = false

        }

        return root
    }

    private fun initBanner() {
        binding?.let { // gunakan safe call operator ?. untuk memeriksa null
            it.progressbar.visibility = View.VISIBLE
            viewModel.banners.observe(viewLifecycleOwner, Observer { items ->
                it.apply {
                    banners(items)
                    progressbar.visibility = View.GONE
                }
            })
            viewModel.loadBanners()
        }
    }


    private fun banners(images: List<SliderModel>) {
        binding.viewpagerSlider.adapter = SliderAdapter(images, binding.viewpagerSlider)
        binding.viewpagerSlider.clipToPadding = false
        binding.viewpagerSlider.clipChildren = true
        binding.viewpagerSlider.offscreenPageLimit = 2
        binding.viewpagerSlider.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        binding.viewpagerSlider.setPageTransformer(ZoomOutPageTransformer())

        if (images.size > 1) {
            binding.indicatorDots.visibility = View.VISIBLE
            binding.indicatorDots.attachTo(binding.viewpagerSlider)
        }
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
                TODO("Not yet implemented")
            }

            override fun onSuggestionClick(position: Int): Boolean {
                val cursor = binding.searchview.suggestionsAdapter.getItem(position) as Cursor
                val columnIndex = cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1)
                if (columnIndex >= 0) {
                    val suggestion = cursor.getString(columnIndex)
                    val action =
                        HomeFragmentDirections.actionNavigationHomeToSearchFragment(suggestion)
                    view?.findNavController()?.navigate(action)
                }
                binding.searchview.setQuery("", false)
                return true
            }

        })

        // Setup search view listener
        binding.searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val action = query?.let {
                    HomeFragmentDirections.actionNavigationHomeToSearchFragment(
                        it
                    )
                }
                if (action != null) {
                    view?.findNavController()?.navigate(action)
                }
                binding.searchview.setQuery("", false)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Perform filtering or updating suggestions here
                val cursor = getSuggestions(newText)
                cursorAdapter.changeCursor(cursor)
                return true
            }
        })
    }

    private fun getSuggestions(query: String?): Cursor {
        val matrixCursor =
            MatrixCursor(arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1))
        if (query != null) {
            // Dummy data for demonstration, replace with real data
            val suggestions = listOf("haloz 1", "rezqi 2", "erigo 3")
            var id = 0
            for (suggestion in suggestions) {
                if (suggestion.contains(query, true)) {
                    matrixCursor.addRow(arrayOf(id++, suggestion))
                }
            }
        }
        return matrixCursor
    }

    private fun explore(){
        val adapter = ExploreAdapter()
        binding.rvExplore.adapter = adapter
        binding.rvExplore.layoutManager = GridLayoutManager(requireContext(), 2)

        viewModel.products.observe(viewLifecycleOwner) { stories ->
            adapter.submitList(stories)
        }
    }

    private fun brand(){
        val adapter2 = BrandAdapterHome { brandName ->
            binding.searchview.setQuery(brandName, true)
        }
        binding.rvBrands.adapter = adapter2
        binding.rvBrands.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        viewModel.brands.observe(viewLifecycleOwner) { brands ->
            adapter2.submitList(brands)
        }
    }

    private fun loading(){
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading) {
                // Tampilkan indikator loading
                binding.progressbar2.visibility = View.VISIBLE
                binding.progressbar3.visibility = View.VISIBLE

            } else {
                // Sembunyikan indikator loading
                binding.progressbar2.visibility = View.GONE
                binding.progressbar3.visibility = View.GONE

            }
        })
    }

    private fun setupView() {

        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (!user.isLogin) {
                binding.tvLokaldesc.visibility = View.VISIBLE
                binding.tvLokaltitle.visibility = View.VISIBLE
                binding.imgUser.visibility = View.VISIBLE
                binding.btnLogin.visibility = View.VISIBLE
                binding.imgLogout.setOnClickListener() {
                    Toast.makeText(requireActivity(),"Anda sudah logout", Toast.LENGTH_SHORT).show()
                }
            } else {
                binding.imgLogout.setOnClickListener() {
                    AlertDialog.Builder(requireActivity()).apply {
                        setTitle("Logout")
                        setMessage("Apakah anda ingin logout")
                        setPositiveButton("Iya") { _, _ ->
                            viewModel.logout()
                            Toast.makeText(requireActivity(),"Berhasil logout", Toast.LENGTH_SHORT).show()
                        }
                        setNegativeButton("Tidak") { _, _ ->

                        }
                        create().show()
                    }
                }
            }
        }

        binding.imgRecycler.setOnClickListener() {
            it.findNavController().navigate(R.id.recycleFragment)
        }

        binding.btnLogin.setOnClickListener() {
            it.findNavController().navigate(R.id.loginFragment)
        }

        setupSearchView()
        initBanner()
        explore()
        brand()
        loading()
        viewModel.allBrands()
        viewModel.allProducts()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}