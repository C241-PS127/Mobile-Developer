package com.example.lokalin.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lokalin.R
import com.example.lokalin.ViewModelFactory
import com.example.lokalin.databinding.FragmentCategoriesBinding
import com.example.lokalin.databinding.FragmentHomeBinding
import com.example.lokalin.ui.categories.CategoriesViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupSearchView()
        setupView()
        refreshApp()


        return root
    }

    private fun setupSearchView() {
        binding.searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    // Navigasi ke SearchFragment dengan query sebagai argumen
                    val action = HomeFragmentDirections.actionNavigationHomeToSearchFragment(query = it)
                    view?.findNavController()?.navigate(action)
                }
                binding.searchview.setQuery("",false)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Tidak perlu melakukan apapun saat query berubah
                return false
            }
        })
    }

    private fun setupView(){
        val adapter = ExploreAdapter()
        binding.rvExplore.adapter = adapter
        binding.rvExplore.layoutManager = GridLayoutManager(requireContext(),2)

        viewModel.products.observe(viewLifecycleOwner) { stories ->
            adapter.submitList(stories)
        }


        binding.imgRecycler.setOnClickListener(){
            it.findNavController().navigate(R.id.recycleFragment)
        }

        binding.cameraButton.setOnClickListener(){
            it.findNavController().navigate(R.id.searchFragment)
        }
    }

    private fun refreshApp(){
        binding.swipeRefreshLayout.setOnRefreshListener{
            setupView()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}