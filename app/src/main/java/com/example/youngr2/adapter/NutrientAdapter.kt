package com.example.youngr2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.youngr2.databinding.ItemSearchResultBinding
import com.example.youngr2.holder.NutrientViewHolder
import com.example.youngr2.models.NutrientRowModel
import com.example.youngr2.utils.NutrientDiffCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class NutrientAdapter(private var repositories : List<NutrientRowModel>) : RecyclerView.Adapter<NutrientViewHolder>() {
    interface OnNutrientClickListener {
        fun onItemClick(position: Int)
    }

    var clickListener : OnNutrientClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NutrientViewHolder {
        val binding = ItemSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false )
        return NutrientViewHolder(binding, clickListener)
    }

    override fun onBindViewHolder(holder: NutrientViewHolder, position: Int) {
        holder.bind(repositories[position])
    }

    override fun getItemCount(): Int {
        return repositories.size
    }

    fun update(updatedRepositories: List<NutrientRowModel>) {
        CoroutineScope(Dispatchers.Main).launch {
            val diffResult = async(Dispatchers.IO) {
                getDiffResult(updatedRepositories)
            }
            repositories = updatedRepositories
            //diffResult 가 반환될때까지 기다렸다가 NutrientAdapter 에 적용
            diffResult.await().dispatchUpdatesTo(this@NutrientAdapter)
        }
    }

    private fun getDiffResult(updated : List<NutrientRowModel>) : DiffUtil.DiffResult {
        val diffCallback = NutrientDiffCallback(repositories, updated)
        return DiffUtil.calculateDiff(diffCallback)
    }

    fun getItem(position: Int) = repositories[position]
}