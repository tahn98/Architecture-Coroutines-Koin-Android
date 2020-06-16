package com.sg.base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sg.base.databinding.ItemMessageBinding
import com.sg.core.model.Message

class MessagePagingAdapter : PagingDataAdapter<Message, MessagePagingAdapter.MessageViewHolder>(
    DIFF_UTIL) {

    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<Message>() {
            override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: Message, newItem: Message
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder(ItemMessageBinding.inflate(LayoutInflater.from(parent.context)))
    }

    inner class MessageViewHolder(private val itemBinding: ItemMessageBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(item: Message?) {
            itemBinding.data = item
        }
    }

}
