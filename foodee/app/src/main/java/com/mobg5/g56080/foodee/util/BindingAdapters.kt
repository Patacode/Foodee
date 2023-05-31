package com.mobg5.g56080.foodee.util

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mobg5.g56080.foodee.R
import com.mobg5.g56080.foodee.dto.domain.FoodProductDomain
import com.mobg5.g56080.foodee.fragment.favorites.FavoriteAdapter
import com.mobg5.g56080.foodee.fragment.favorites.FavoriteEntry
import com.mobg5.g56080.foodee.fragment.foodproduct.FoodProductAdapter
import com.mobg5.g56080.foodee.fragment.foodproduct.FoodProductViewModel
import com.mobg5.g56080.foodee.fragment.fridge.FridgeAdapter
import com.mobg5.g56080.foodee.fragment.fridge.FridgeEntry

@BindingAdapter("imgStatus")
fun bindStatus(statusImageView: ImageView,
               status: FoodProductViewModel.LoadingStatus?){

    when(status){
        FoodProductViewModel.LoadingStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        FoodProductViewModel.LoadingStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        FoodProductViewModel.LoadingStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
        else -> {
            statusImageView.visibility = View.GONE
        }
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("statusText")
fun bindStatusText(textView: TextView, foodProductDomain: FoodProductDomain?){
    foodProductDomain?.let {
        if(it.statusCode > 0){ // valid
            textView.visibility = View.GONE
        } else{ // invalid
            textView.visibility = View.VISIBLE
            textView.text = "No product found"
        }
    }
}

@BindingAdapter("productStatus")
fun bindStatusProduct(layout: ConstraintLayout, foodProductDomain: FoodProductDomain?) {
    foodProductDomain?.let {
        if(it.statusCode > 0){ // valid
            layout.visibility = View.VISIBLE
        } else{ // invalid
            layout.visibility = View.GONE
        }
    }
}

@BindingAdapter("imgSrcUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image))
            .into(imgView)
    }
}

@BindingAdapter("listData")
fun bindDetailData(recyclerView: RecyclerView, foodProductDomain: FoodProductDomain?){
    val adapter = recyclerView.adapter as FoodProductAdapter
    adapter.submitList(foodProductDomain?.asEntryPairList())
}

@BindingAdapter("listFavorites")
fun bindFavoriteData(recyclerView: RecyclerView, items: List<FavoriteEntry>?){
    val adapter = recyclerView.adapter as FavoriteAdapter
    adapter.submitList(items)
}

@BindingAdapter("listFridgeItem")
fun bindFrigeData(recyclerView: RecyclerView, items: List<FridgeEntry>?){
    val adapter = recyclerView.adapter as FridgeAdapter
    adapter.submitList(items)
}

@BindingAdapter("productContent")
fun bindDetailText(textView: TextView, text: String?){
    val newText: StringBuilder = StringBuilder("")
    newText.append(
        if(text != null){
            if(text.length > 10)
                StringBuilder(text.substring(0, 10).trim()).append("...")
            else text
        } else "??"
    )

    textView.text = newText.toString()
}
