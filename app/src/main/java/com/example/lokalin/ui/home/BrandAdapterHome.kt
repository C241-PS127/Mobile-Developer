package com.example.lokalin.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lokalin.R
import com.example.lokalin.databinding.OfficialBrandBinding
import com.example.response.Brand

class BrandAdapterHome(
    private val searchListener: (String) -> Unit
) : ListAdapter<Brand, BrandAdapterHome.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            OfficialBrandBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, searchListener)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    class MyViewHolder(
        private val binding: OfficialBrandBinding, private val searchListener: (String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: Brand) {
            Glide.with(binding.root).load(user.logoUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.baseline_assignment_24).into(binding.imgProduct)

            binding.root.setOnClickListener {
                searchListener(user.brandName)
            }
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