package com.nutrient.youngr2.util

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.nutrient.youngr2.remote.models.ParsedProductListItemModel

class NoopListCallback : ListUpdateCallback {
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
}

class MyDiffCallback : DiffUtil.ItemCallback<ParsedProductListItemModel>() {
    override fun areItemsTheSame(oldItem: ParsedProductListItemModel, newItem: ParsedProductListItemModel): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ParsedProductListItemModel, newItem: ParsedProductListItemModel): Boolean {
        return oldItem == newItem
    }
}