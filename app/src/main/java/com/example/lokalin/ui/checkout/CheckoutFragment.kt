package com.example.lokalin.ui.checkout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.lokalin.R
import com.example.lokalin.ViewModelFactory
import com.example.lokalin.databinding.FragmentCartBinding
import com.example.lokalin.databinding.FragmentCheckoutBinding
import com.example.lokalin.ui.cart.CartViewModel

class CheckoutFragment : Fragment() {

    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!
    private val args: CheckoutFragmentArgs by navArgs()
    private val viewModel by viewModels<CheckoutViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private lateinit var tokenUser: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cartId = args.cartId
        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (user.isLogin) {
                tokenUser = user.token
                val freight = 2
                val shipperId = "d74066e4-4b28-4a0b-aa2a-dc7ea42d51b4"
                val paymentId = "bc3527e1-77d8-477b-9b98-aae56c163e6e"

                binding.buttonSubmit.setOnClickListener(){
                    viewModel.addOrder(tokenUser,cartId,paymentId,freight,shipperId)
                }
                Toast.makeText(requireContext(),"CartId : $tokenUser",Toast.LENGTH_SHORT).show()
            }
        }
    }

}
