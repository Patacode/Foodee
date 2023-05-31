package com.mobg5.g56080.foodee.fragment.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mobg5.g56080.foodee.databinding.FavoriteProductViewItemBinding

class FavoriteAdapter(private val favoriteViewModel: FavoriteViewModel): ListAdapter<FavoriteEntry, FavoriteAdapter.ViewHolder>(
    FavoriteDiffCallback()
){

    class ViewHolder private constructor(val binding: FavoriteProductViewItemBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(item: FavoriteEntry, viewModel: FavoriteViewModel) {
            binding.favoriteItem = item
            binding.favoriteArrow.setOnClickListener{ viewModel.onShowProduct(item) }
            binding.favoriteTrash.setOnClickListener { viewModel.onFavoriteDelete(item) }
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FavoriteProductViewItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, favoriteViewModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }
}

class FavoriteDiffCallback : DiffUtil.ItemCallback<FavoriteEntry>() {
    override fun areItemsTheSame(oldItem: FavoriteEntry, newItem: FavoriteEntry): Boolean {
        return oldItem.barcode == newItem.barcode
    }

    override fun areContentsTheSame(oldItem: FavoriteEntry, newItem: FavoriteEntry): Boolean {
        return oldItem == newItem
    }
}