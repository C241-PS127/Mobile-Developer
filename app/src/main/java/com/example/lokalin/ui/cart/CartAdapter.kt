package com.example.lokalin.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lokalin.R
import com.example.lokalin.databinding.ProductCartBinding
import com.example.lokalin.ui.cart.CartViewModel
import com.example.response.CartResponseItem
import java.text.NumberFormat
import java.util.Locale

class CartAdapter(private val viewModel: CartViewModel, private val token: String) :
    ListAdapter<CartResponseItem, CartAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ProductCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, viewModel, token)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }

    fun calculateTotalPrice(): Int {
        var total = 0
        for (i in 0 until itemCount) {
            val item = getItem(i)
            total += item.unitPrice?.times(item.count!!) ?: 0
        }
        return total
    }

    class MyViewHolder(
        private val binding: ProductCartBinding,
        private val viewModel: CartViewModel,
        private val token: String
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: CartResponseItem) {
            binding.apply {
                tvType.text = data.productName
                val total = data.unitPrice?.times(data.count!!)
                tvPrice.text = total?.let { formatRupiah(it) }
                tvBrand.text = data.brandName
                txtCountItem.text = data.count.toString()
                Glide.with(binding.root).load(data.imgUrl)
                    .error(R.drawable.round_button_enabled).into(binding.imgProduct)
            }

            binding.btnDel.setOnClickListener {
                AlertDialog.Builder(itemView.context).apply {
                    setTitle("Delete")
                    setMessage("Apakah anda ingin delete")
                    setPositiveButton("Iya") { _, _ ->
                        viewModel.deleteCart(token, data.cartId.toString())
                        viewModel.allCart(token)
                        Toast.makeText(itemView.context, "Berhasil delete", Toast.LENGTH_SHORT).show()
                        Log.d("TAG", "HASILLL ${viewModel.cart.value}")
                    }
                    setNegativeButton("Tidak") { _, _ ->
                        // Tidak melakukan apa-apa jika tombol "Tidak" diklik
                    }
                    create().show()
                }
            }
        }

        private fun formatRupiah(amount: Int): String {
            val localeID = Locale("in", "ID")
            val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
            return formatRupiah.format(amount)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CartResponseItem>() {
            override fun areItemsTheSame(oldItem: CartResponseItem, newItem: CartResponseItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: CartResponseItem, newItem: CartResponseItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
