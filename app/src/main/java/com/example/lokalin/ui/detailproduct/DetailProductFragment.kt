package com.example.lokalin.ui.detailproduct

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.lokalin.R
import com.example.lokalin.ViewModelFactory
import com.example.lokalin.databinding.FragmentDetailProductBinding
import com.example.lokalin.ui.categories.CategoriesFragmentDirections
import com.example.lokalin.ui.wishlist.WishlistViewModel
import com.example.response.Product
import com.example.utils.ResultState
import java.text.NumberFormat
import java.util.Locale

class DetailProductFragment : Fragment() {

    private var _binding: FragmentDetailProductBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private val wishlistViewModel by viewModels<WishlistViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var productId: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailProductBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val args: DetailProductFragmentArgs by navArgs()
        productId = args.ProductId

        viewModel.fetchProductDetail(productId)

        viewModel.productDetail.observe(viewLifecycleOwner) {
            setDetail(it)
        }

        var statusFav = checkWishlistStatus(productId)
        var wishlistId = getWishlistIdByProductId(productId)
        if (statusFav) {
            binding.btnFav.setImageResource(R.drawable.baseline_favorite_24)
        } else {
            binding.btnFav.setImageResource(R.drawable.baseline_favorite_red_24)
        }

//        var token: String
//
        wishlistViewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (user.isLogin) {
                setupAction(user.token)
            }else{
                setupAction(user.token)
            }
        }

        binding.btnFav.setOnClickListener {
            if (statusFav) {
                // wishlistViewModel.deleteWishlist(token, wishlistId!!)
                binding.btnFav.setImageResource(R.drawable.baseline_favorite_24)
            } else {
                //  favoriteUserViewModel.insert(favoriteUser)
                binding.btnFav.setImageResource(R.drawable.baseline_favorite_red_24)
            }
        }

        setupQuantity()

        return root
    }

    fun checkWishlistStatus(productId: String): Boolean {
        val wishlist = wishlistViewModel.wishlist.value
        wishlist?.forEach { item ->
            if (item.productId == productId) {
                return true
            }
        }
        return false
    }

    fun getWishlistIdByProductId(productId: String): String? {
        val wishlist = wishlistViewModel.wishlist.value
        wishlist?.forEach { item ->
            if (item.productId == productId) {
                return item.wishlistId
            }
        }
        return null
    }

    fun setupQuantity() {
        var quantity = 0

        binding.btnPlus.setOnClickListener {
            quantity += 1
            binding.tvQuantity.text = quantity.toString()
        }

        binding.btnMinus.setOnClickListener {
            if (quantity > 0) {
                quantity -= 1
                binding.tvQuantity.text = quantity.toString()
            }
        }
    }

    fun setupAction(token: String) {
        binding.btnAddToCart.setOnClickListener {
            val quantity = binding.tvQuantity.text.toString().toIntOrNull() ?: 1

            viewModel.addCart(token, productId, quantity)
            viewModel.cart.observe(viewLifecycleOwner) { resultState ->
                when (resultState) {

                    is ResultState.Success -> {
                        Toast.makeText(requireContext(), "Berhasil menambah cart", Toast.LENGTH_SHORT).show()
                    }

                    is ResultState.Error -> {
                        Toast.makeText(requireContext(), "Gagal menambah cart", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        // Handle loading state if needed
                    }
                }
            }
        }
    }

    private fun setDetail(product: Product) {
        binding.apply {
            toolbarTitle.text = product.brandName
            tvDetailName.text = product.productName
            tvDetailType.text = product.categoryName
            tvDetailDescription.text = product.productDescription
            tvDetailPrice.text = formatRupiah(product.unitPrice.toInt())

        }
    }

    private fun formatRupiah(amount: Int): String {
        val localeID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
        return formatRupiah.format(amount)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}