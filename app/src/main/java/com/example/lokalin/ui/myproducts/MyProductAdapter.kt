package com.example.lokalin.ui.myproducts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lokalin.R
import com.example.lokalin.databinding.WishlistItemBinding
import com.example.response.ProductsItem
import java.text.NumberFormat
import java.util.Locale

class MyProductAdapter : ListAdapter<ProductsItem, MyProductAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = WishlistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        if (user != null) {
            holder.bind(user)
        }
    }

    class MyViewHolder(private val binding: WishlistItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: ProductsItem) {

            binding.apply {
                tvPrice.text = user.unitPrice?.let { formatRupiah(it.toInt()) }
                tvBrand.text = user.brandName
                tvType.text = user.productName
                Glide.with(itemView.context)
                    .load(user.imgUrl)
                    .error(R.drawable.erigo)
                    .into(imgProduct)
            }

        }

        private fun formatRupiah(amount: Int): String {
            val localeID = Locale("in", "ID")
            val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
            return formatRupiah.format(amount)
        }

    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProductsItem>() {
            override fun areItemsTheSame(oldItem: ProductsItem, newItem: ProductsItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ProductsItem, newItem: ProductsItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}