package com.example.lokalin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lokalin.R
import com.example.lokalin.databinding.ProductBinding
import com.example.data.response.ProductsItem
import com.example.lokalin.ui.home.HomeFragmentDirections
import java.text.NumberFormat
import java.util.Locale

class ExploreAdapter :  PagingDataAdapter<ProductsItem, ExploreAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        if (user != null) {
            holder.bind(user)
        }
    }

    class MyViewHolder(private val binding: ProductBinding) : RecyclerView.ViewHolder(binding.root) {
        private val brandTextView: TextView = itemView.findViewById(R.id.txt_brand)
        private val priceTextView: TextView = itemView.findViewById(R.id.txt_price)
        private val typeTextView: TextView = itemView.findViewById(R.id.txt_type)
        private val productImageView: ImageView = itemView.findViewById(R.id.img_product)

        fun bind(user: ProductsItem) {
            brandTextView.text = user.unitPrice?.let { formatRupiah(it.toInt()) }
            priceTextView.text = user.productName
            typeTextView.text = user.categoryName
            Glide.with(itemView.context)
                .load(user.imgUrl)
                .error(R.drawable.erigo)
                .into(productImageView)

            binding.root.setOnClickListener(){
                val action = user.productId.let { it1 ->
                    HomeFragmentDirections.actionNavigationHomeToDetailProductFragment(
                        it1
                    )
                }
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