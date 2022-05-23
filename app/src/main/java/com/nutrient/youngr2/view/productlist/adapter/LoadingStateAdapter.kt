package com.nutrient.youngr2.view.productlist.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nutrient.youngr2.databinding.ItemLoadingBinding

class LoadingStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<LoadingStateAdapter.LoadingStateViewHolder>() {

    companion object {
        const val TAG = "LoadingStateAdapter"
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ) : LoadingStateAdapter.LoadingStateViewHolder{
        val binding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadingStateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LoadingStateViewHolder, loadState: LoadState) =
        holder.bind(loadState)

    inner class LoadingStateViewHolder(
        private val binding : ItemLoadingBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.tvReload.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState) {
            Log.d(TAG, "loadingState $loadState")
            binding.apply {
                progressBar.isVisible = loadState is LoadState.Loading
                linearError.isVisible = loadState is LoadState.Error
            }
        }
    }
}