package com.example.lokalin.ui.home

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lokalin.R
import com.example.lokalin.databinding.ProductBinding
import com.example.response.Product
import java.text.NumberFormat
import java.util.Locale

class ExploreAdapter : ListAdapter<Product, ExploreAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    class MyViewHolder(private val binding: ProductBinding) : RecyclerView.ViewHolder(binding.root) {
        private val brandTextView: TextView = itemView.findViewById(R.id.txt_brand)
        private val priceTextView: TextView = itemView.findViewById(R.id.txt_price)
        private val typeTextView: TextView = itemView.findViewById(R.id.txt_type)
        private val productImageView: ImageView = itemView.findViewById(R.id.img_product)

        fun bind(user: Product) {
            brandTextView.text = formatRupiah(user.unitPrice.toInt())
            priceTextView.text = user.productName
            typeTextView.text = user.categoryName
            val pictures = user.pictures // Ini adalah list URL gambar dari respons API
            val imageUrl = pictures.firstOrNull()
            Glide.with(itemView.context)
                .load(imageUrl)
                .error(R.drawable.erigo)
                .into(productImageView)

            binding.root.setOnClickListener(){
                val action = HomeFragmentDirections.actionNavigationHomeToDetailProductFragment(user.productId)
                it.findNavController().navigate(action)
            }
        }

        private fun formatRupiah(amount: Int): String {
            val localeID = Locale("in", "ID")
            val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
            return formatRupiah.format(amount)
        }



    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: Product, newItem: Product
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}