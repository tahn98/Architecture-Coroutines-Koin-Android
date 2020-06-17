package com.sg.base.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sg.base.databinding.ItemLoadStateBinding

class BaseLoadStateAdapter(private val retry: (() -> Unit)? = null) :
    LoadStateAdapter<BaseLoadStateAdapter.LoadStateViewHolder>() {

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val itemBinding =
            ItemLoadStateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadStateViewHolder(itemBinding)
    }

    inner class LoadStateViewHolder(private val itemBinding: ItemLoadStateBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(loadState: LoadState?) {
            if (loadState is LoadState.Error) {
                itemBinding.errorMsg.text = loadState.error.message
            }
            itemBinding.progressBar.visibility = toVisibility(loadState == LoadState.Loading)
            itemBinding.retryButton.visibility = toVisibility(loadState != LoadState.Loading)
            itemBinding.errorMsg.visibility = toVisibility(loadState != LoadState.Loading)

            itemBinding.retryButton.setOnClickListener {
                retry?.invoke()
            }
        }

        private fun toVisibility(constraint: Boolean): Int = if (constraint) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

}