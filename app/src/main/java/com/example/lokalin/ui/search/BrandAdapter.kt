package com.example.lokalin.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lokalin.R
import com.example.lokalin.databinding.OfficialBrandSearchBinding
import com.example.lokalin.databinding.ProductBinding
import com.example.lokalin.ui.search.BrandAdapter.MyViewHolder.Companion.DIFF_CALLBACK
import com.example.response.Brand
import com.example.response.Product

class BrandAdapter : ListAdapter<Brand, BrandAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            OfficialBrandSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    class MyViewHolder(private val binding: OfficialBrandSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val brandTextView: TextView = itemView.findViewById(R.id.tv_brands)

        fun bind(user: Brand) {
            brandTextView.text = user.brandName

            binding.root.setOnClickListener(){
                TODO("Masukkan implementasi API search")
            }

        }

        companion object {
            val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Brand>() {
                override fun areItemsTheSame(oldItem: Brand, newItem: Brand): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(
                    oldItem: Brand, newItem: Brand
                ): Boolean {
                    return oldItem == newItem
                }
            }
        }
    }
}