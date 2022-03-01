package com.example.youngr2.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.youngr2.models.NutrientRowModel

class NutrientDiffCallback(private val oldList : List<NutrientRowModel>, private val newList : List<NutrientRowModel>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = oldList[oldItemPosition].food_code == newList[newItemPosition].food_code

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = oldList[oldItemPosition] == newList[newItemPosition]
}