package com.example.lokalin.ui.detailproduct

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.lokalin.R
import com.example.lokalin.ViewModelFactory
import com.example.lokalin.databinding.FragmentDetailProductBinding
import com.example.lokalin.ui.cart.CartViewModel
import com.example.lokalin.ui.categories.CategoriesFragmentDirections
import com.example.lokalin.ui.wishlist.WishlistFragmentDirections
import com.example.lokalin.ui.wishlist.WishlistViewModel
import com.example.response.ProductsItem
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

    private val cartViewModel by viewModels<CartViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var productId: String

    private lateinit var tokenUser: String // Jadikan global agar bisa diakses di seluruh kelas

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailProductBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupView()

        binding.swipeRefresh.setOnRefreshListener {
            setupView()
            binding.swipeRefresh.isRefreshing = false

        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        wishlistViewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (user.isLogin) {
                tokenUser = user.token
                cartViewModel.allCart(user.token)
                setupAction(user.token)
                wishlistViewModel.allWishlist(tokenUser)
            }
        }

        wishlistViewModel.wishlist.observe(viewLifecycleOwner) { wishlist ->
            var statusFav = checkWishlistStatus(productId)
            var wishlistId = getWishlistIdByProductId(productId)

            if (statusFav) {
                binding.btnFav.setImageResource(R.drawable.baseline_favorite_red_24)
            } else {
                binding.btnFav.setImageResource(R.drawable.baseline_favorite_24)
            }

            binding.btnFav.setOnClickListener {
                if (statusFav) {
                    wishlistViewModel.deleteWishlist(tokenUser, wishlistId!!)
                    binding.btnFav.setImageResource(R.drawable.baseline_favorite_24)
                    Toast.makeText(requireContext(), "Berhasil menghapus wishlist", Toast.LENGTH_SHORT).show()
                } else {
                    wishlistViewModel.addWishlist(tokenUser, productId)
                    binding.btnFav.setImageResource(R.drawable.baseline_favorite_red_24)
                    Toast.makeText(requireContext(), "Berhasil menambah wishlist", Toast.LENGTH_SHORT).show()
                }
            }
        }

        setupQuantity()
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

    fun checkCartStatus(productId: String): Boolean {
        val cart = cartViewModel.cart.value
        cart?.forEach { item ->
            if (item.productId == productId) {
                return true
            }
        }
        return false
    }

    fun getCartIdByProductId(productId: String): String? {
        val cart = cartViewModel.cart.value
        cart?.forEach { item ->
            if (item.productId == productId) {
                return item.cartId
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
            var statusCart = checkCartStatus(productId)
            val cartId = getCartIdByProductId(productId)
            if(!statusCart){
                viewModel.addCart(token, productId, quantity)
                cartViewModel.allCart(token)
                Toast.makeText(requireContext(), "Berhasil menambah cart", Toast.LENGTH_SHORT).show()

                findNavController().navigate(R.id.navigation_cart)
                findNavController().popBackStack(R.id.detailProductFragment, true)

            } else {
                if (cartId != null) {
                    cartViewModel.updateCart(token, cartId, quantity)
                    Toast.makeText(requireContext(), "Berhasil menambah cart", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.navigation_cart)
                    findNavController().popBackStack(R.id.detailProductFragment, true)
                }
            }
        }
    }

    private fun setDetail(product: ProductsItem) {
        binding.apply {
            toolbarTitle.text = product.brandName
            tvDetailName.text = product.productName
            tvDetailType.text = product.categoryName
            tvDetailDescription.text = product.productDescription
            Glide.with(requireActivity()).load(product.imgUrl).into(imgProduct)
            tvDetailPrice.text = product.unitPrice?.let { formatRupiah(it.toInt()) }

        }
    }

    private fun loading(){
        viewModel.loading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading) {
                // Tampilkan indikator loading
                binding.progressIndicator.visibility = View.VISIBLE
            } else {
                // Sembunyikan indikator loading
                binding.progressIndicator.visibility = View.GONE
            }
        })
    }

    private fun setupView(){
        val args: DetailProductFragmentArgs by navArgs()
        productId = args.ProductId

        viewModel.fetchProductDetail(productId)

        viewModel.productDetail.observe(viewLifecycleOwner) {
            setDetail(it)
        }

        loading()
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