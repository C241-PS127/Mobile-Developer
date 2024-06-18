package com.example.lokalin.ui.checkout

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lokalin.ViewModelFactory
import com.example.lokalin.databinding.FragmentCheckoutBinding
import com.example.lokalin.ui.cart.CartViewModel
import com.example.lokalin.adapter.CartAdapter
import java.text.NumberFormat
import java.util.Locale

class CheckoutFragment : Fragment() {

    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!
    private val args: CheckoutFragmentArgs by navArgs()
    private val viewModel by viewModels<CheckoutViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private val cartViewModel by viewModels<CartViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private lateinit var tokenUser: String
    private lateinit var paymentAdapter: ArrayAdapter<String>
    private lateinit var selectedPaymentId: String

    private lateinit var shippersAdapter: ArrayAdapter<String>
    private lateinit var selectedShippersId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cartId = args.cartId

        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (user.isLogin) {
                tokenUser = user.token

                setupAutoCompleteTextView()
                setupAction(tokenUser)
                viewModel.allCart(tokenUser)

                binding.uploadButton.setOnClickListener {
                    val freight = 1
                    viewModel.addOrder(tokenUser, cartId, selectedPaymentId, freight, selectedShippersId)
                    Toast.makeText(requireContext(), "Berhasil menambah cart", Toast.LENGTH_SHORT).show()
                    binding.root.postDelayed({
                        findNavController().navigateUp()
                    }, 500)
                }
            }
        }
        binding.backButton.setOnClickListener(){
            findNavController().navigateUp()
        }


        viewModel.getPayments()
        viewModel.getShippers()
    }

    private fun setupAutoCompleteTextView() {
        paymentAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            mutableListOf()
        )
        binding.categoryAutoCompleteTextView.setAdapter(paymentAdapter)

        viewModel.payment.observe(viewLifecycleOwner) { payments ->
            payments?.let {
                val paymentTypes = it.map { payment -> payment.PaymentType }
                paymentAdapter.clear()
                paymentAdapter.addAll(paymentTypes)
                paymentAdapter.notifyDataSetChanged()
            }
        }

        binding.categoryAutoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            val selectedPaymentType = parent.getItemAtPosition(position) as String
            val selectedPayment = viewModel.payment.value?.find { it.PaymentType == selectedPaymentType }
            selectedPayment?.let {
                selectedPaymentId = it.PaymentId
                Log.d("CheckoutFragment", "Selected payment ID: $selectedPaymentId")
            }
        }

        binding.categoryAutoCompleteTextView.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                binding.categoryAutoCompleteTextView.showDropDown()
            }
        }

        shippersAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            mutableListOf()
        )
        binding.shippersAutoCompleteTextView.setAdapter(shippersAdapter)

        viewModel.shippers.observe(viewLifecycleOwner) { payments ->
            payments?.let {
                val paymentTypes = it.map { payment -> payment.companyName }
                shippersAdapter.clear()
                shippersAdapter.addAll(paymentTypes)
                shippersAdapter.notifyDataSetChanged()
            }
        }

        binding.shippersAutoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            val selectedPaymentType = parent.getItemAtPosition(position) as String
            val selectedPayment = viewModel.shippers.value?.find { it.companyName == selectedPaymentType }
            selectedPayment?.let {
                selectedShippersId = it.shipperId
                Log.d("CheckoutFragment", "Selected Shipper ID: $selectedShippersId")
            }
        }

        binding.shippersAutoCompleteTextView.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                binding.shippersAutoCompleteTextView.showDropDown()
            }
        }
    }

    private fun setupAction(token: String) {
        val adapter = CartAdapter(cartViewModel, token)
        binding.recCart.adapter = adapter
        val layoutManager = LinearLayoutManager(requireContext())
        binding.recCart.layoutManager = layoutManager

        viewModel.cart.observe(viewLifecycleOwner) { cart ->
            Log.d("TAG", "Hasil Cartku ${cart}")
            if (cart?.isNotEmpty() == true) {
                adapter.submitList(cart)
                binding.priceProductEditText.setText(cart[0].unitPrice?.let { formatRupiah(it) })

            }
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
