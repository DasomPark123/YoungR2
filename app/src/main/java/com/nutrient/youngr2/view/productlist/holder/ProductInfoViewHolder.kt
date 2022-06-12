package com.nutrient.youngr2.view.productlist.holder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nutrient.youngr2.R
import com.nutrient.youngr2.view.productlist.adapter.ProductInfoAdapter
import com.nutrient.youngr2.databinding.ItemSearchResultBinding
import com.nutrient.youngr2.remote.models.ParsedProductListItemModel

class ProductInfoViewHolder(private val viewBinding : ItemSearchResultBinding, private val clickListener : ProductInfoAdapter.OnProductClickListener?) : RecyclerView.ViewHolder(viewBinding.root) {
    fun bind(parsedProductInfoModel : ParsedProductListItemModel) {
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