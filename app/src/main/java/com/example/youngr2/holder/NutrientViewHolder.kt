package com.example.youngr2.holder

import androidx.recyclerview.widget.RecyclerView
import com.example.youngr2.adapter.NutrientAdapter
import com.example.youngr2.databinding.ItemSearchResultBinding
import com.example.youngr2.models.NutrientRowModel

class NutrientViewHolder(private val viewBinding : ItemSearchResultBinding, clickListener : NutrientAdapter.OnNutrientClickListener?) : RecyclerView.ViewHolder(viewBinding.root) {

    init {
        viewBinding.llSearchResult.setOnClickListener {
            clickListener?.onItemClick(adapterPosition)
        }
    }

    fun bind(nutrientRowModel : NutrientRowModel) {
        viewBinding.nutrient = nutrientRowModel
    }
}