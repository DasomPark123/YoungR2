package com.nutrient.youngr2.views.product_list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.nutrient.youngr2.databinding.ItemSearchResultBinding
import com.nutrient.youngr2.views.product_list.holder.ProductListViewHolder
import com.nutrient.youngr2.remote.models.ParsedProductListItemModel

class ProductListAdapter (private val clickListener: OnProductClickListener) : PagingDataAdapter<ParsedProductListItemModel, ProductListViewHolder>(PRODUCT_INFO_COMPARATOR) {

    interface OnProductClickListener {
        fun onItemClick(data : ParsedProductListItemModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductListViewHolder {
        val binding =
            ItemSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductListViewHolder(binding, clickListener)
    }

    override fun onBindViewHolder(holder: ProductListViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {  holder.bind(item) }
    }

    companion object {
        private val PRODUCT_INFO_COMPARATOR = object : DiffUtil.ItemCallback<ParsedProductListItemModel>() {
            override fun areItemsTheSame(
                oldItem: ParsedProductListItemModel,
                newItem: ParsedProductListItemModel
            ): Boolean = oldItem.productId == newItem.productId

            override fun areContentsTheSame(
                oldItem: ParsedProductListItemModel,
                newItem: ParsedProductListItemModel
            ): Boolean = oldItem == newItem
        }
    }
}