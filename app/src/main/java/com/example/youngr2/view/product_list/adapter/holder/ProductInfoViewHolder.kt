package com.example.youngr2.view.product_list.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.youngr2.R
import com.example.youngr2.view.product_list.adapter.ProductInfoAdapter
import com.example.youngr2.databinding.ItemSearchResultBinding
import com.example.youngr2.remote.models.ParsedProductInfoModel

class ProductInfoViewHolder(private val viewBinding : ItemSearchResultBinding, private val clickListener : ProductInfoAdapter.OnProductClickListener?) : RecyclerView.ViewHolder(viewBinding.root) {
    fun bind(parsedProductInfoModel : ParsedProductInfoModel) {
        viewBinding.apply {
            productInfo = parsedProductInfoModel
            Glide.with(itemView)
                .load(parsedProductInfoModel.imageUrl)
                .error(R.drawable.ic_no_image)
                .override(300,300)
                .into(ivProduct)
            llSearchResult.setOnClickListener {
                clickListener?.onItemClick(parsedProductInfoModel)
            }
        }
    }
}