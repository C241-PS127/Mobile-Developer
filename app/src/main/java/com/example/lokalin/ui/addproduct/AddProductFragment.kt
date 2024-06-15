package com.example.lokalin.ui.addproduct

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.api.ApiConfig
import com.example.di.Injection
import com.example.lokalin.R
import com.example.lokalin.ViewModelFactory
import com.example.lokalin.databinding.FragmentAddProductBinding
import com.example.lokalin.ui.categories.CategoriesViewModel
import com.example.lokalin.ui.profile.ProfileViewModel
import com.example.lokalin.ui.search.SearchViewModel
import com.example.storyapp.data.pref.UserModel
import com.example.utils.ResultState
import com.example.utils.reduceFileImage
import com.example.utils.uriToFile


class AddProductFragment : Fragment() {
    private var _binding: FragmentAddProductBinding? = null
    private val binding get() = _binding!!

    private var currentImageUri: Uri? = null

    private lateinit var productName: String
    private lateinit var productDescription: String
    private var brandId: String? = null
    private lateinit var categoryId: String
    private var unitPrice: Int? = 0
    private lateinit var unitSize: String
    private var unitInStock: Int? = 0
    private var isAvailable: Boolean? = false
    private lateinit var rating: String

    private val categoryViewModel by viewModels<CategoriesViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private val addProductViewModel by viewModels<AddProductViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private val profileViewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private val searchViewModel by viewModels<SearchViewModel> {
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
        binding.galleryButton.setOnClickListener { startGallery() }

        binding.uploadButton.setOnClickListener { uploadProduct() }

        return root
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
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
            }
        }

        binding.categoryAutoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            val selectedCategoryName = parent.getItemAtPosition(position) as String
            val selectedCategory =
                categoryViewModel.categories.value?.find { it.categoryName == selectedCategoryName }
            selectedCategory?.let {
                categoryId = it.categoryId!!
                Log.d("TAG", "Selected category ID: ${categoryId}")
            }
        }

        binding.categoryAutoCompleteTextView.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                binding.categoryAutoCompleteTextView.showDropDown()
            }
        }

        val availableSwitch: SwitchCompat = binding.availableSwitch

        availableSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                isAvailable = true
            } else {
                isAvailable = false
            }
        }

        profileViewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (user.isLogin) {
                profile(user.token)
            }
        }
    }

    private fun profile(token: String) {
        profileViewModel.getProfile(token)
        profileViewModel.profile.observe(viewLifecycleOwner, Observer {
            var brandName = it[0].fullName.toString()
            searchViewModel.allBrands()
            searchViewModel.brands.observe(viewLifecycleOwner, Observer {
                val cart = searchViewModel.brands.value
                cart?.forEach { item ->
                    if (item.brandName == brandName) {
                        brandId = item.brandId
                    }
                }
            })

        })
    }


    private fun uploadProduct() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, requireContext()).reduceFileImage()
            productName = binding.nameEditText.text.toString()
            productDescription = binding.descProductEditText.text.toString()
            unitPrice = binding.priceProductEditText.text.toString().toIntOrNull()
            unitSize = binding.sizeProductEditText.text.toString()
            unitInStock = binding.stockProductEditText.text.toString().toIntOrNull()
            rating = binding.ratingProductEditText.text.toString()

            addProductViewModel.addProduct(
                productName,
                productDescription,
                brandId!!,
                categoryId,
                unitPrice!!,
                unitSize,
                unitInStock!!,
                isAvailable!!,
                rating,
                imageFile
            ).observe(viewLifecycleOwner) { resultState ->
                when (resultState) {
                    is ResultState.Loading -> {
                        //  showLoading(true)
                    }

                    is ResultState.Success -> {
                        binding.apply {
                            previewImageView.setImageResource(R.drawable.ic_place_holder)
                            nameEditText.text?.clear()
                            descProductEditText.text?.clear()
                            priceProductEditText.text?.clear()
                            sizeProductEditText.text?.clear()
                            stockProductEditText.text?.clear()
                            ratingProductEditText.text?.clear()
                            availableSwitch.isChecked = false
                            categoryAutoCompleteTextView.text.clear()
                        }
                        showToast("Berhasil menambah")
                    }

                    is ResultState.Error -> {
                        showToast(resultState.error)
                        // showLoading(false)
                    }

                    else -> {

                    }
                }
            }

        } ?: showToast("URL empty")

    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
