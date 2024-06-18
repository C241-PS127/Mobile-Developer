package com.example.lokalin.ui.home

import android.app.SearchManager
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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lokalin.R
import com.example.lokalin.ViewModelFactory
import com.example.lokalin.databinding.FragmentHomeBinding
import com.example.data.response.SliderModel
import com.example.lokalin.adapter.BrandAdapterHome
import com.example.lokalin.adapter.ExploreAdapter
import com.example.lokalin.adapter.SliderAdapter
import com.example.utils.ZoomOutPageTransformer
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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
        binding?.let {
            it.progressbar.visibility = View.VISIBLE
            viewModel.banners.observe(viewLifecycleOwner, Observer { items ->
                it.apply {
                    banners(items)
                    progressbar.visibility = View.GONE
                }
            })
            viewModel.loadBanners()
        }
        binding?.let {
            it.progressbar4.visibility = View.VISIBLE
            viewModel.banner2.observe(viewLifecycleOwner, Observer { items ->
                it.apply {
                    bannerss(items)
                    progressbar3.visibility = View.GONE
                }
            })
            viewModel.loadBannerss()
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

    private fun bannerss(images: List<SliderModel>) {
        binding.viewpagerSlider2.adapter = SliderAdapter(images, binding.viewpagerSlider2)
        binding.viewpagerSlider2.clipToPadding = false
        binding.viewpagerSlider2.clipChildren = true
        binding.viewpagerSlider2.offscreenPageLimit = 2
        binding.viewpagerSlider2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        binding.viewpagerSlider2.setPageTransformer(ZoomOutPageTransformer())

        if (images.size > 1) {
            binding.indicatorDots2.visibility = View.VISIBLE
            binding.indicatorDots2.attachTo(binding.viewpagerSlider2)
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

    private fun explore() {
        val adapter = ExploreAdapter()
        binding.rvExplore.adapter = adapter
        binding.rvExplore.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.productss?.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }
    }

    private fun explores() {
        val adapter = ExploreAdapter()
        binding.rvExplore2.adapter = adapter
        binding.rvExplore2.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.productsss?.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }
    }

    private fun brand() {
        val adapter2 = BrandAdapterHome { brandName ->
            binding.searchview.setQuery(brandName, true)
        }
        binding.rvBrands.adapter = adapter2
        binding.rvBrands.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        viewModel.brands.observe(viewLifecycleOwner) { brands ->
            adapter2.submitList(brands)
        }
    }


    private fun loading() {
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading) {
                binding.progressbar2.visibility = View.VISIBLE

            } else {
                binding.progressbar2.visibility = View.GONE

            }
        })
        viewModel.isLoading2.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading) {
                binding.progressbar3.visibility = View.VISIBLE
                binding.progressbar5.visibility = View.VISIBLE


            } else {
                // Sembunyikan indikator loading
                binding.progressbar3.visibility = View.GONE
                binding.progressbar5.visibility = View.GONE

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
                    Toast.makeText(requireActivity(), "Anda sudah logout", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                binding.imgLogout.setOnClickListener() {
                    AlertDialog.Builder(requireActivity()).apply {
                        setTitle("Logout")
                        setMessage("Apakah anda ingin logout")
                        setPositiveButton("Iya") { _, _ ->
                            viewModel.logout()
                            Toast.makeText(requireActivity(), "Berhasil logout", Toast.LENGTH_SHORT)
                                .show()
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
        explores()
        brand()
        loading()
        viewModel.allBrands()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}