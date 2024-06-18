package com.example.lokalin.ui.Order.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lokalin.ViewModelFactory
import com.example.lokalin.databinding.FragmentHistory2Binding
import com.example.lokalin.adapter.OrderAdapter

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistory2Binding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<HistoryViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private lateinit var tokenUser: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistory2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = OrderAdapter()
        binding.recyclerViewOrders.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewOrders.adapter = adapter

        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (user.isLogin) {
                tokenUser = user.token
                viewModel.fetchOrdersFinished(tokenUser)
            }
        }

        viewModel.ordersProcessing.observe(viewLifecycleOwner) { orders ->
            if (orders?.isNotEmpty() == true) {
                adapter.submitList(orders)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
