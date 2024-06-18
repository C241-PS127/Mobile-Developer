package com.example.lokalin.ui.shoporders

import OrdersItem
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lokalin.R
import com.example.lokalin.databinding.ItemShoporderBinding
import java.text.NumberFormat
import java.util.Locale

class ShopOrdersAdapter(private val shopOrdersViewModel: ShopOrdersViewModel) : ListAdapter<OrdersItem, ShopOrdersAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemShoporderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, shopOrdersViewModel)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        if (user != null) {
            holder.bind(user)
        }
    }

    class MyViewHolder(private val binding: ItemShoporderBinding, private val shopOrdersViewModel: ShopOrdersViewModel) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var statusAdapter: ArrayAdapter<String>
        private lateinit var statusName: String

        fun bind(data: OrdersItem) {

            binding.apply {
                tvCustomer.text = data.fullName
                tvPayment.text = data.paymentType
                tvCompany.text = "(${data.companyName})"
                tvType.text = data.productName
                tvPrice.text = data.price?.let { formatRupiah(it.toInt()) }
                tvStatus.text = data.statusName
                Glide.with(itemView.context)
                    .load(data.imgUrl)
                    .error(R.drawable.erigo)
                    .into(imgProduct)
            }

            statusAdapter = ArrayAdapter(
                itemView.context,
                android.R.layout.simple_dropdown_item_1line,
                mutableListOf()
            )
            binding.statusAutoCompleteTextView.setAdapter(statusAdapter)

            shopOrdersViewModel.status.observe(itemView.context as LifecycleOwner) { status ->
                status?.let {
                    val statusNames = it.map { status -> status.statusName }
                    statusAdapter.clear()
                    statusAdapter.addAll(statusNames)
                    statusAdapter.notifyDataSetChanged()
                }
            }

            binding.statusAutoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
                val selectedStatusName = parent.getItemAtPosition(position) as String
                val selectedStatus =
                    shopOrdersViewModel.status.value?.find { it.statusName == selectedStatusName }
                selectedStatus?.let {
                    statusName = it.statusName!!
                    Log.d("TAG", "Selected category ID: ${statusName}")
                }
            }

            binding.statusAutoCompleteTextView.setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    binding.statusAutoCompleteTextView.showDropDown()
                }
            }

            binding.txtStatus.setOnClickListener{
                binding.apply{
                    txtStatus.visibility = View.INVISIBLE
                    editButton.visibility = View.VISIBLE
                    cancelButton.visibility = View.VISIBLE
                    statusInputLayout.visibility = View.VISIBLE
                }
            }

            binding.cancelButton.setOnClickListener {
                binding.apply {
                    txtStatus.visibility = View.VISIBLE
                    editButton.visibility = View.GONE
                    cancelButton.visibility = View.GONE
                    statusInputLayout.visibility = View.GONE
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
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<OrdersItem>() {
            override fun areItemsTheSame(oldItem: OrdersItem, newItem: OrdersItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: OrdersItem, newItem: OrdersItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}