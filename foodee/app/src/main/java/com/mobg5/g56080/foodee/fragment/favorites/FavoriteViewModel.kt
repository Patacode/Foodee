package com.mobg5.g56080.foodee.fragment.favorites

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mobg5.g56080.foodee.util.status.Status

class FavoriteViewModel: ViewModel(){

    private val _items = MutableLiveData<List<FavoriteEntry>>()
    val items: LiveData<List<FavoriteEntry>>
        get() = _items

    private val _onShowProduct = MutableLiveData<FavoriteEntry>()
    val onShowProduct: LiveData<FavoriteEntry>
        get() = _onShowProduct

    private val _onShowFirebaseSnackbar = MutableLiveData<Status>()
    val onShowFirebaseSnackbar: LiveData<Status>
        get() = _onShowFirebaseSnackbar

    private val _showFallbackMessage = MutableLiveData<Int>()
    val showFallbackMessage: LiveData<Int>
        get() = _showFallbackMessage

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    init{
        loadFavorites(mAuth.currentUser)
    }

    fun onShowProduct(item: FavoriteEntry){
        _onShowProduct.value = item
    }

    fun onFavoriteDelete(item: FavoriteEntry){
        mAuth.currentUser?.let {
            Firebase.firestore.collection("favorites")
                .whereEqualTo("user", it.uid)
                .whereEqualTo("barcode", item.barcode)
                .get()
                .addOnSuccessListener { query ->
                    query.documents.forEach { doc -> doc.reference.delete() }
                    val message = "Product ${item.title} successfully deleted !"
                    loadFavorites(mAuth.currentUser)
                    onShowFirebaseSnackbar(Status[Status.State.VALID], message)
                }.addOnFailureListener { exc ->
                    val message = exc.message
                    val fallbackMessage: String = message ?: "An error occurred"
                    onShowFirebaseSnackbar(Status[Status.State.INVALID], fallbackMessage)
                }
        }
    }

    fun onShowFirebaseSnackbar(status: Status, message: String){
        _onShowFirebaseSnackbar.value = status with message
    }

    private fun loadFavorites(user: FirebaseUser?){
        user?.let {
            Firebase.firestore.collection("favorites")
                .whereEqualTo("user", it.uid)
                .get()
                .addOnSuccessListener { query ->
                    if(query.documents.isEmpty()){
                        _showFallbackMessage.value = View.VISIBLE
                    } else{
                        _showFallbackMessage.value = View.GONE
                    }

                    _items.value = query.documents.map { doc ->
                        val imageUrl = doc["image_url"] as String? ?: "??"
                        val productTitle = doc["product_title"] as String? ?: "??"
                        val barcode = doc["barcode"] as String
                        FavoriteEntry(imageUrl, productTitle, barcode)
                    }
                }
        }
    }
}
