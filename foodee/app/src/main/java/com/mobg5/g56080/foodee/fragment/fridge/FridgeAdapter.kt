package com.mobg5.g56080.foodee.fragment.fridge

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mobg5.g56080.foodee.databinding.FridgeProductViewItemBinding

class FridgeAdapter(private val fridgeViewModel: FridgeViewModel): ListAdapter<FridgeEntry, FridgeAdapter.ViewHolder>(
    FridgeDiffCallback()
){

    class ViewHolder private constructor(val binding: FridgeProductViewItemBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(item: FridgeEntry, viewModel: FridgeViewModel) {
            binding.fridgeItem = item
            binding.fridgeItemArrow.setOnClickListener{ viewModel.onShowProduct(item) }
            binding.fridgeItemTrash.setOnClickListener { viewModel.onFridgeItemDelete(item) }
            binding.fridgeItemReload.setOnClickListener { viewModel.onFridgeItemUpdate(item) }
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FridgeProductViewItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, fridgeViewModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }
}

class FridgeDiffCallback : DiffUtil.ItemCallback<FridgeEntry>() {
    override fun areItemsTheSame(oldItem: FridgeEntry, newItem: FridgeEntry): Boolean {
        return oldItem.barcode == newItem.barcode
    }

    override fun areContentsTheSame(oldItem: FridgeEntry, newItem: FridgeEntry): Boolean {
        return oldItem == newItem
    }
}