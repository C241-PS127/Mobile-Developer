package com.example.lokalin.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.request.RequestOptions
import com.example.lokalin.R
import com.example.response.SliderModel

class SliderAdapter(private var sliderItem: List<SliderModel>, private val viewPager2: ViewPager2) :
    RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {
    private lateinit var context: Context
    private val runables = Runnable {
    }

    class SliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageSlider)
        fun setImage(sliderItems: SliderModel, context: Context) {
            val requestOptions = RequestOptions().transform(CenterInside())

            Glide.with(context).load(sliderItems.url).apply(requestOptions).into(imageView)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): SliderAdapter.SliderViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.slider_item_container, parent, false)
        return SliderViewHolder(view)
    }


    override fun onBindViewHolder(holder: SliderAdapter.SliderViewHolder, position: Int) {
        holder.setImage(sliderItem[position], context)
        if (position == sliderItem.lastIndex - 1) {
            viewPager2.post(runables)
        }
    }

    override fun getItemCount(): Int = sliderItem.size
}