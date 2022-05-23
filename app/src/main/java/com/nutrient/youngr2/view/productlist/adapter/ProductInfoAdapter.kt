package com.nutrient.youngr2.view.productlist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.nutrient.youngr2.databinding.ItemSearchResultBinding
import com.nutrient.youngr2.view.productlist.adapter.holder.ProductInfoViewHolder
import com.nutrient.youngr2.remote.models.ParsedProductInfoModel

class ProductInfoAdapter(private val clickListener: OnProductClickListener) : PagingDataAdapter<ParsedProductInfoModel, ProductInfoViewHolder>(PRODUCT_INFO_COMPARATOR) {

    interface OnProductClickListener {
        fun onItemClick(data : ParsedProductInfoModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductInfoViewHolder {
        val binding =
            ItemSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductInfoViewHolder(binding, clickListener)
    }

    override fun onBindViewHolder(holder: ProductInfoViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {  holder.bind(item) }
    }
    companion object {
        private val PRODUCT_INFO_COMPARATOR = object : DiffUtil.ItemCallback<ParsedProductInfoModel>() {
            override fun areItemsTheSame(
                oldItem: ParsedProductInfoModel,
                newItem: ParsedProductInfoModel
            ): Boolean = oldItem.productId == newItem.productId

            override fun areContentsTheSame(
                oldItem: ParsedProductInfoModel,
                newItem: ParsedProductInfoModel
            ): Boolean = oldItem == newItem
        }
    }
}