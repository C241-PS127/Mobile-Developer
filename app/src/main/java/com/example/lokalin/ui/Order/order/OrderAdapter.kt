package com.example.lokalin.ui.home

import OrdersItem
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lokalin.R
import com.example.lokalin.databinding.ItemOrderBinding
import java.text.NumberFormat
import java.util.Locale

class OrderAdapter : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    private val orders = mutableListOf<OrdersItem>()

    inner class OrderViewHolder(private val binding: ItemOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(order: OrdersItem) {
            binding.tvPrice.text = order.price?.let { formatRupiah(it.toInt()) }
            binding.tvBrand.text = order.companyName
            binding.tvPayment.text = order.paymentType
            binding.tvType.text = order.productName
            binding.tvProcess.text = order.statusName

            Glide.with(binding.imgProduct.context)
                .load(order.imgUrl)
                .into(binding.imgProduct)
        }

        private fun formatRupiah(amount: Int): String {
            val localeID = Locale("in", "ID")
            val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
            return formatRupiah.format(amount)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemOrderBinding.inflate(inflater, parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(orders[position])
    }

    override fun getItemCount(): Int = orders.size

    fun submitList(newOrders: List<OrdersItem>) {
        orders.clear()
        orders.addAll(newOrders)
        notifyDataSetChanged()
    }
}
