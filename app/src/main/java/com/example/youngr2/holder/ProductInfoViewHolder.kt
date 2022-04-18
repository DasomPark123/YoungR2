package com.example.youngr2.holder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.youngr2.R
import com.example.youngr2.adapter.ProductInfoAdapter
import com.example.youngr2.databinding.ItemSearchResultBinding
import com.example.youngr2.models.ProductListItemModel

class ProductInfoViewHolder(private val viewBinding : ItemSearchResultBinding, private val clickListener : ProductInfoAdapter.OnProductClickListener?) : RecyclerView.ViewHolder(viewBinding.root) {
    fun bind(productListItemModel : ProductListItemModel) {
        viewBinding.apply {
            productInfo = productListItemModel
            Glide.with(itemView)
                .load(productListItemModel.imgurl1)
                .error(R.drawable.ic_no_image)
                .override(200,200)
                .into(ivProduct)
            llSearchResult.setOnClickListener {
                clickListener?.onItemClick(productListItemModel)
            }
        }
    }
}