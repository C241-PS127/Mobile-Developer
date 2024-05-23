package com.example.lokalin.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.lokalin.databinding.FragmentCartBinding
import com.example.lokalin.ui.cart.CartViewModel

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val cartViewModel =
            ViewModelProvider(this).get(CartViewModel::class.java)

        _binding = FragmentCartBinding.inflate(inflater, container, false)
        val root: View = binding.root



        binding.swipeRefreshLayout.setOnRefreshListener {
            // Lakukan tindakan untuk memuat ulang data
            // Misalnya, muat ulang data dari server atau database lokal
            loadData()
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun loadData() {
        // Misalnya, muat ulang data dari server atau database lokal
        // Setelah selesai, berhentikan animasi refresh
        binding.swipeRefreshLayout.isRefreshing = false
    }
}