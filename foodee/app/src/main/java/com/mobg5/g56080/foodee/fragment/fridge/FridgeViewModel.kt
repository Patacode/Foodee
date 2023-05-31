package com.mobg5.g56080.foodee.fragment.fridge

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mobg5.g56080.foodee.fragment.favorites.FavoriteEntry
import com.mobg5.g56080.foodee.util.chain.DateChain
import com.mobg5.g56080.foodee.util.chain.link.DayChainLink
import com.mobg5.g56080.foodee.util.chain.link.MonthChainLink
import com.mobg5.g56080.foodee.util.chain.link.YearChainLink
import com.mobg5.g56080.foodee.util.status.Status

class FridgeViewModel: ViewModel() {

    private val _items = MutableLiveData<List<FridgeEntry>>()
    val items: LiveData<List<FridgeEntry>>
        get() = _items

    private val _onShowProduct = MutableLiveData<FridgeEntry>()
    val onShowProduct: LiveData<FridgeEntry>
        get() = _onShowProduct

    private val _onShowFirebaseSnackbar = MutableLiveData<Status>()
    val onShowFirebaseSnackbar: LiveData<Status>
        get() = _onShowFirebaseSnackbar

    private val _showFallbackMessage = MutableLiveData<Int>()
    val showFallbackMessage: LiveData<Int>
        get() = _showFallbackMessage

    private val _onOpenPopupWindow = MutableLiveData<FridgeEntry>()
    val onOpenPopupWindow: LiveData<FridgeEntry>
        get() = _onOpenPopupWindow

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    init{
        loadFridgeItems(mAuth.currentUser)
    }

    fun onShowProduct(item: FridgeEntry){
        _onShowProduct.value = item
    }

    fun onFridgeItemDelete(item: FridgeEntry){
        mAuth.currentUser?.let {
            Firebase.firestore.collection("fridges")
                .whereEqualTo("user", it.uid)
                .whereEqualTo("barcode", item.barcode)
                .get()
                .addOnSuccessListener { query ->
                    query.documents.forEach { doc -> doc.reference.delete() }
                    val message = "Fridge item ${item.title} successfully deleted !"
                    loadFridgeItems(mAuth.currentUser)
                    onShowFirebaseSnackbar(Status[Status.State.VALID], message)
                }.addOnFailureListener { exc ->
                    val message = exc.message
                    val fallbackMessage: String = message ?: "An error occurred"
                    onShowFirebaseSnackbar(Status[Status.State.INVALID], fallbackMessage)
                }
        }
    }

    fun onFridgeItemUpdate(item: FridgeEntry){
        _onOpenPopupWindow.value = item
    }

    fun updateFridgeItem(item: FridgeEntry, expirationDate: String){
        DateChain(expirationDate)
            .onSuccess {
                mAuth.currentUser?.let {
                    Firebase.firestore.collection("fridges")
                        .whereEqualTo("user", it.uid)
                        .whereEqualTo("barcode", item.barcode)
                        .get()
                        .addOnSuccessListener { query ->
                            query.documents.forEach { doc ->
                                doc.reference.update(
                                    mapOf("expiration_date" to expirationDate)
                                )
                            }

                            val message = "Fridge item ${item.title} successfully updated !"
                            loadFridgeItems(mAuth.currentUser)
                            onShowFirebaseSnackbar(Status[Status.State.VALID], message)
                        }.addOnFailureListener { exc ->
                            val message = exc.message
                            val fallbackMessage: String = message ?: "An error occurred"
                            onShowFirebaseSnackbar(Status[Status.State.INVALID], fallbackMessage)
                        }
                }
            }.onFailure {
                val message = when(it){
                    is YearChainLink -> "Invalid year, must be >= 2000"
                    is MonthChainLink -> "Invalid month, must be > 0 and <= 12"
                    is DayChainLink -> "Invalid day for the given year and month"
                    else -> "Invalid"
                }
                onShowFirebaseSnackbar(Status[Status.State.INVALID], message)
            }.run()
    }

    fun onShowFirebaseSnackbar(status: Status, message: String){
        _onShowFirebaseSnackbar.value = status with message
    }

    private fun loadFridgeItems(user: FirebaseUser?){
        user?.let {
            Firebase.firestore.collection("fridges")
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
                        val expDate = doc["expiration_date"] as String
                        FridgeEntry(imageUrl, productTitle, barcode, expDate)
                    }.sortedBy { v -> v.expiration_date }
                }
        }
    }
}