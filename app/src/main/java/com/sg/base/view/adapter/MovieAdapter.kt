package com.sg.base.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sg.base.R
import com.sg.base.databinding.BriefDescriptionLayoutBinding
import com.sg.core.model.Movie

class NowPlayingAdapter(private val onItemClickListener: (Movie, Int) -> Unit) :
    ListAdapter<Movie, NowPlayingAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        var DIFF_CALLBACK: DiffUtil.ItemCallback<Movie> =
            object : DiffUtil.ItemCallback<Movie>() {
                override fun areItemsTheSame(
                    oldItem: Movie,
                    newItem: Movie
                ): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(
                    oldItem: Movie,
                    newItem: Movie
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<BriefDescriptionLayoutBinding>(
            LayoutInflater.from(parent.context),
            R.layout.brief_description_layout,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position], position)
        holder.binding.root.setOnClickListener {
            onItemClickListener.invoke(currentList[position], position)
        }
    }


    override fun getItemViewType(position: Int): Int {
        return R.layout.brief_description_layout
    }

    inner class ViewHolder(val binding: BriefDescriptionLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            movie: Movie?,
            position: Int
        ) {
            binding.model = movie
            binding.executePendingBindings()
        }
    }

}