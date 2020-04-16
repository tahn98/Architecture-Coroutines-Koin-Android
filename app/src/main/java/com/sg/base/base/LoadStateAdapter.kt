package com.sg.base.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sg.base.R
import com.sg.base.databinding.ItemLoadStateBinding
import com.sg.core.model.Result
import com.sg.core.vo.ItemViewModel

class LoadStateAdapter(private val retry: (() -> Unit)? = null) :
    RecyclerView.Adapter<LoadStateAdapter.LoadStateViewHolder>() {

    var loadState: Result<*>? = null
        get() = field
        set(value) {
            if (field != value) {
                val displayOldItem = displayLoadStateAsItem(field)
                val displayNewItem = displayLoadStateAsItem(value)
                if (displayOldItem && !displayNewItem) {
                    notifyItemRemoved(0)
                } else if (displayNewItem && !displayOldItem) {
                    notifyItemInserted(0)
                } else if (displayOldItem && displayNewItem) {
                    notifyItemChanged(0)
                }
                field = value
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoadStateViewHolder {
        val itemBinding = DataBindingUtil.inflate<ItemLoadStateBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_load_state,
            parent,
            false
        )
        return LoadStateViewHolder(itemBinding)
    }

    override fun getItemCount(): Int = if (displayLoadStateAsItem(loadState)) 1 else 0

    override fun onBindViewHolder(holder: LoadStateViewHolder, position: Int) {
        holder.bind(loadState)
    }

    inner class LoadStateViewHolder(private val itemBinding: ItemLoadStateBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(loadState: Result<*>?) {
            if (loadState is Result.Error) {
                itemBinding.errorMsg.text = loadState.message
            }
            itemBinding.progressBar.visibility = toVisibility(loadState == Result.LoadingMore)
            itemBinding.retryButton.visibility = toVisibility(loadState != Result.LoadingMore)
            itemBinding.errorMsg.visibility = toVisibility(loadState != Result.LoadingMore)
        }

        private fun toVisibility(constraint: Boolean): Int = if (constraint) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun displayLoadStateAsItem(loadState: Result<*>?): Boolean {
        return loadState is Result.LoadingMore || loadState is Result.Error
    }

}