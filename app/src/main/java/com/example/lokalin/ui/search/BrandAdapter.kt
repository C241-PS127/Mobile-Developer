package com.example.lokalin.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lokalin.R
import com.example.lokalin.databinding.OfficialBrandSearchBinding
import com.example.lokalin.ui.search.BrandAdapter.MyViewHolder.Companion.DIFF_CALLBACK
import com.example.response.Brand

class BrandAdapter(
    private val searchListener: (String) -> Unit
) : ListAdapter<Brand, BrandAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            OfficialBrandSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, searchListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    class MyViewHolder(
        private val binding: OfficialBrandSearchBinding,
        private val searchListener: (String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        private val brandTextView: TextView = itemView.findViewById(R.id.tv_brands)

        fun bind(user: Brand) {
            brandTextView.text = user.brandName

            binding.root.setOnClickListener {
                searchListener(user.brandName)
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
