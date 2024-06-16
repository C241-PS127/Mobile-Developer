package com.example.lokalin.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lokalin.R
import com.example.lokalin.ViewModelFactory
import com.example.lokalin.databinding.FragmentCartBinding
import com.example.lokalin.ui.cart.CartViewModel
import java.text.NumberFormat
import java.util.Locale

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null

    private val viewModel by viewModels<CartViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        val root: View = binding.root



        binding.swipeRefreshLayout.setOnRefreshListener {
            setupView()
            binding.swipeRefreshLayout.isRefreshing = false

        }

        setupView()

        return root
    }

    private fun setupAction(token: String) {
        val adapter = CartAdapter(viewModel, token)
        binding.recCart.adapter = adapter
        val layoutManager = LinearLayoutManager(requireContext())
        binding.recCart.layoutManager = layoutManager

        viewModel.cart.observe(viewLifecycleOwner) { cart ->
            Log.d("TAG", "Hasil Cartku ${cart}")
            if (cart?.isNotEmpty() == true) {
                adapter.submitList(cart)

                val totalPrice = adapter.calculateTotalPrice()
                binding.tvSubtotalPrice.text = formatRupiah(totalPrice.toLong())

                calculateAndDisplayTotal()
            }
        }
    }

    private fun calculateAndDisplayTotal() {
        val subtotal = extractPrice(binding.tvSubtotalPrice.text.toString())
        val fee = extractPrice(binding.tvFeePrice.text.toString())
        val applicationFee = extractPrice(binding.tvApplicationFeePrice.text.toString())
        val total = subtotal + fee + applicationFee
        val totalRupiah = total.toDouble() / 100
        binding.tvTotalPrice.text = formatRupiah(totalRupiah.toLong())
    }

    private fun extractPrice(priceText: String): Long {
        return try {
            val cleanString = priceText.replace(Regex("[Rp,.\\s]"), "")

            val price = cleanString.toLong()
            price
        } catch (e: NumberFormatException) {
            0L
        }
    }

    private fun load(){
        viewModel.loading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading) {
                // Tampilkan indikator loading
                binding.progressBar2.visibility = View.VISIBLE
            } else {
                // Sembunyikan indikator loading
                binding.progressBar2.visibility = View.GONE
            }
        })

    }

    private fun loadCart(){
        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (user.isLogin) {
                setupAction(user.token)
                viewModel.allCart(user.token)
            } else {
                Toast.makeText(requireContext(), "You are not logged in", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun formatRupiah(amount: Long): String {
        val localeID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
        return formatRupiah.format(amount)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setupView() {
        loadCart()
        load()

        binding.historyImg.setOnClickListener() {
            it.findNavController().navigate(R.id.historyFragment)
        }

        binding.btnCheckout.setOnClickListener {
            viewModel.cart.observe(viewLifecycleOwner) { carts ->
                carts?.firstOrNull()?.let { cartItem ->
                    val cartId = cartItem.cartId

                    cartId?.let {
                        val action = CartFragmentDirections.actionNavigationCartToCheckoutFragment(it)
                        findNavController().navigate(action)
                    }
                }
            }
        }


    }
}