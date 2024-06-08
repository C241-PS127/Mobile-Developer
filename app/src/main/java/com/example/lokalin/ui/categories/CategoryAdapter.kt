package com.example.lokalin.ui.home

import android.app.Activity
import android.content.Intent
import android.util.Log
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
import com.example.lokalin.databinding.ItemCategoryBinding
import com.example.lokalin.databinding.ProductBinding
import com.example.lokalin.ui.categories.CategoriesFragmentDirections
import com.example.response.CategoryResponseItem
import com.example.response.Product
import java.text.NumberFormat
import java.util.Locale

class CategoryAdapter : ListAdapter<CategoryResponseItem, CategoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }

    class MyViewHolder(private val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        private val categoryTextView: TextView = itemView.findViewById(R.id.categoryName)
        fun bind(data: CategoryResponseItem) {
            categoryTextView.text = data.categoryName

            binding.root.setOnClickListener(){
//                val action = CategoriesFragmentDirections.actionNavigationCategoriesToNavigationProductbycategory(data.categoryId!!)
//                it.findNavController().navigate(action)
                Log.d("coba", "Hanya nyoba apa sudah bisa diklik")
            }
        }

    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CategoryResponseItem>() {
            override fun areItemsTheSame(oldItem: CategoryResponseItem, newItem: CategoryResponseItem): Boolean {
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