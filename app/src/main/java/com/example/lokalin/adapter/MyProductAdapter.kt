package com.example.lokalin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lokalin.R
import com.example.lokalin.databinding.WishlistItemBinding
import com.example.data.response.ProductsItem
import com.example.lokalin.ui.myproducts.MyProductViewModel
import java.text.NumberFormat
import java.util.Locale

class MyProductAdapter(private val viewModel: MyProductViewModel) : ListAdapter<ProductsItem, MyProductAdapter.MyViewHolder>(
    DIFF_CALLBACK
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = WishlistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, viewModel)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        if (user != null) {
            holder.bind(user)
        }
    }

    class MyViewHolder(private val binding: WishlistItemBinding, private val viewModel: MyProductViewModel) : RecyclerView.ViewHolder(binding.root) {
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

            binding.btnDel.setOnClickListener() {

                AlertDialog.Builder(itemView.context).apply {
                    setTitle("Delete")
                    setMessage("Apakah anda ingin delete")
                    setPositiveButton("Iya") { _, _ ->
                        viewModel.deleteProduct(user.productId.toString())
                        viewModel.getProductsByBrand(user.brandName)
                        Toast.makeText(itemView.context, "Berhasil delete", Toast.LENGTH_SHORT)
                            .show()
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