package com.example.youngr2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.youngr2.databinding.ItemSearchResultBinding
import com.example.youngr2.holder.ProductInfoViewHolder
import com.example.youngr2.models.ProductListItemModel

class ProductInfoAdapter(private val clickListener: OnProductClickListener) : PagingDataAdapter<ProductListItemModel, ProductInfoViewHolder>(PRODUCT_INFO_COMPARATOR) {

    interface OnProductClickListener {
        fun onItemClick(data : ProductListItemModel)
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
        private val PRODUCT_INFO_COMPARATOR = object : DiffUtil.ItemCallback<ProductListItemModel>() {
            override fun areItemsTheSame(
                oldItem: ProductListItemModel,
                newItem: ProductListItemModel
            ): Boolean = oldItem.prdlstReportNo == newItem.prdlstReportNo

            override fun areContentsTheSame(
                oldItem: ProductListItemModel,
                newItem: ProductListItemModel
            ): Boolean = oldItem == newItem
        }
    }
}