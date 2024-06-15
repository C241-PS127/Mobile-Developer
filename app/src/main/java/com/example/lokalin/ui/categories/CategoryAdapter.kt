package com.example.lokalin.ui.home

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lokalin.R
import com.example.lokalin.databinding.ItemCategoryBinding
import com.example.lokalin.databinding.ProductBinding
import com.example.lokalin.ui.categories.CategoriesFragmentDirections
import com.example.response.CategoryResponseItem
import java.text.NumberFormat
import java.util.Locale

class CategoryAdapter( private val searchListener: (String) -> Unit) :
    ListAdapter<CategoryResponseItem, CategoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, searchListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }

    class MyViewHolder(private val binding: ItemCategoryBinding, private val searchListener: (String) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        private val categoryTextView: TextView = itemView.findViewById(R.id.categoryName)
        fun bind(data: CategoryResponseItem) {
            categoryTextView.text = data.categoryName

            when (data.categoryName) {
                "Polo Shirt" -> {
                    Glide.with(binding.root).load(R.drawable.poloshirt).into(binding.categoryImage)
                }
                "Hoodie" -> {
                    Glide.with(binding.root).load(R.drawable.hoodie).into(binding.categoryImage)
                }
                "T-Shirt" -> {
                    Glide.with(binding.root).load(R.drawable.tshirt).into(binding.categoryImage)
                }
                "Bag" -> {
                    Glide.with(binding.root).load(R.drawable.bag).into(binding.categoryImage)
                }
                "Shirt" -> {
                    Glide.with(binding.root).load(R.drawable.shirt).into(binding.categoryImage)
                }
                "Pant" -> {
                    Glide.with(binding.root).load(R.drawable.pants).into(binding.categoryImage)
                }
                else -> {
                    Glide.with(binding.root).load(R.drawable.ic_launcher_foreground).into(binding.categoryImage)
                }
            }

            binding.root.setOnClickListener() {
                searchListener(data.categoryName.toString())
            }
        }

    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CategoryResponseItem>() {
            override fun areItemsTheSame(
                oldItem: CategoryResponseItem,
                newItem: CategoryResponseItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: CategoryResponseItem, newItem: CategoryResponseItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}