package com.example.lokalin.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.RadioButton
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lokalin.R
import com.example.lokalin.ViewModelFactory
import com.example.lokalin.databinding.FragmentRecycleBinding
import com.example.lokalin.databinding.FragmentSearchBinding
import com.example.lokalin.ui.home.ExploreAdapter
import com.example.lokalin.ui.home.HomeViewModel
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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.imgBack.setOnClickListener(){
            findNavController().navigateUp()
        }

        setFilterData()

        val adapter = BrandAdapter()
        binding.rvBrands.adapter = adapter
        binding.rvBrands.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        viewModel.brands.observe(viewLifecycleOwner) { stories ->
            adapter.submitList(stories)
        }

        val args: SearchFragmentArgs by navArgs()
        val query = args.query

        binding.searchview.setQuery(query, false)


        return root
    }

    private fun Search(){

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


}