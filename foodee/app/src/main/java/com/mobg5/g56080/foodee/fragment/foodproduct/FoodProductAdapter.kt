package com.mobg5.g56080.foodee.fragment.foodproduct

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mobg5.g56080.foodee.databinding.ProductDetailViewItemBinding

class FoodProductAdapter: ListAdapter<FoodProductEntry, FoodProductAdapter.ViewHolder>(
    FoodProductDiffCallback()
){

    class ViewHolder private constructor(val binding: ProductDetailViewItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: FoodProductEntry) {
            binding.detailLabel.text = item.entryName
            binding.detailContent.text = if(item.entryContent == null || item.entryContent.isEmpty()) "??" else item.entryContent
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ProductDetailViewItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }
}

class FoodProductDiffCallback : DiffUtil.ItemCallback<FoodProductEntry>() {
    override fun areItemsTheSame(oldItem: FoodProductEntry, newItem: FoodProductEntry): Boolean {
        return oldItem.entryName == newItem.entryName
    }

    override fun areContentsTheSame(oldItem: FoodProductEntry, newItem: FoodProductEntry): Boolean {
        return oldItem == newItem
    }
}
