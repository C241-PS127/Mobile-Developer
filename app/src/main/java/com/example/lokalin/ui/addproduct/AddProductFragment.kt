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
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.lokalin.ViewModelFactory
import com.example.lokalin.databinding.FragmentAddProductBinding
import com.example.lokalin.ui.categories.CategoriesViewModel
import com.example.lokalin.ui.profile.ProfileViewModel
import com.example.utils.reduceFileImage
import com.example.utils.uriToFile


class AddProductFragment : Fragment() {
    private var _binding: FragmentAddProductBinding? = null
    private val binding get() = _binding!!

    private var currentImageUri: Uri? = null

    private lateinit var productName: String
    private lateinit var productDescription: String
    private lateinit var brandId: String
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
            val selectedCategory = categoryViewModel.categories.value?.find { it.categoryName == selectedCategoryName }
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

        profileViewModel.profile.observe(viewLifecycleOwner, Observer {
            binding.apply {
            }


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


           // addProductViewModel.addProduct(imageFile, description)

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
