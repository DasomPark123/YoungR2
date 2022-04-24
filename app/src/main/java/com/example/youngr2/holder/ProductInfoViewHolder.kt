package com.example.youngr2.holder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.youngr2.R
import com.example.youngr2.adapter.ProductInfoAdapter
import com.example.youngr2.databinding.ItemSearchResultBinding
import com.example.youngr2.models.ParsedProductInfo

class ProductInfoViewHolder(private val viewBinding : ItemSearchResultBinding, private val clickListener : ProductInfoAdapter.OnProductClickListener?) : RecyclerView.ViewHolder(viewBinding.root) {
    fun bind(parsedProductInfo : ParsedProductInfo) {
        viewBinding.apply {
            productInfo = parsedProductInfo
            Glide.with(itemView)
                .load(parsedProductInfo.imageUrl)
                .error(R.drawable.ic_no_image)
                .override(300,300)
                .into(ivProduct)
            llSearchResult.setOnClickListener {
                clickListener?.onItemClick(parsedProductInfo)
            }
        }
    }
}