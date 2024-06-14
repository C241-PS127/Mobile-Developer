package com.example.lokalin.ui.shop

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.lokalin.R
import com.example.lokalin.databinding.FragmentProfileBinding
import com.example.lokalin.databinding.FragmentShopBinding

class ShopFragment : Fragment() {
    private var _binding: FragmentShopBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShopBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupView()

        return root
    }


    private fun setupView(){
        binding.apply {
            layAddproduct.setOnClickListener {
                findNavController().navigate(R.id.addProductFragment)
            }
            layMyproduct.setOnClickListener {
                findNavController().navigate(R.id.myProductFragment)
            }
        }

    }

}