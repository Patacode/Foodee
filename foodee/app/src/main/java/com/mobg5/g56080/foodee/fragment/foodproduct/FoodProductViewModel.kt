package com.mobg5.g56080.foodee.fragment.foodproduct

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mobg5.g56080.foodee.R
import com.mobg5.g56080.foodee.database.foodproduct.FoodProductRepository
import com.mobg5.g56080.foodee.dto.domain.FoodProductDomain
import com.mobg5.g56080.foodee.util.chain.DateChain
import com.mobg5.g56080.foodee.util.chain.link.*
import com.mobg5.g56080.foodee.util.status.Status
import kotlinx.coroutines.launch

class FoodProductViewModel(
    private val foodProductRepository: FoodProductRepository,
    private val barcode: String,
): ViewModel() {

    enum class LoadingStatus{
        LOADING,
        ERROR,
        DONE,
    }

    private lateinit var _rawFoodProduct: FoodProductDomain
    private val _foodProduct = MutableLiveData<FoodProductDomain>()
    val foodProduct: LiveData<FoodProductDomain>
        get() = _foodProduct

    private val _status = MutableLiveData<LoadingStatus>()
    val status: LiveData<LoadingStatus>
        get() = _status

    private val _onChangeFavoriteIcon = MutableLiveData<Int>()
    val onChangeFavoriteIcon: LiveData<Int>
        get() = _onChangeFavoriteIcon

    private val _onChangeFridgeIcon = MutableLiveData<Int>()
    val onChangeFridgeIcon: LiveData<Int>
        get() = _onChangeFridgeIcon

    private val _onShowFirebaseSnackbar = MutableLiveData<Status>()
    val onShowFirebaseSnackbar: LiveData<Status>
        get() = _onShowFirebaseSnackbar

    private val _onMakeUnclickableFavoriteIcon = MutableLiveData<Boolean>()
    val onMakeUnclickableFavoriteIcon: LiveData<Boolean>
        get() = _onMakeUnclickableFavoriteIcon

    private val _onMakeUnclickableFridgeIcon = MutableLiveData<Boolean>()
    val onMakeUnclickableFridgeIcon: LiveData<Boolean>
        get() = _onMakeUnclickableFridgeIcon

    private val _onOpenPopupWindow = MutableLiveData<Boolean>()
    val onOpenPopupWindow: LiveData<Boolean>
        get() = _onOpenPopupWindow

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    init{
        loadProductInformations()
        checkProductInFavorites(mAuth.currentUser)
        checkProductInFridge(mAuth.currentUser)
    }

    fun onClickFavoriteIcon(){
        val currentUser: FirebaseUser? = mAuth.currentUser
        if(currentUser != null){ // user is connected
            addProductInFavorites(currentUser)
        } else{ // user is not connected
            val message = "You must be connected to perform this action"
            onShowFirebaseSnackbar(Status[Status.State.INVALID], message)
        }
    }

    fun onClickFridgeIcon(){
        val currentUser: FirebaseUser? = mAuth.currentUser
        if(currentUser != null){ // user is connected
            _onOpenPopupWindow.value = true
        } else{ // user is not connected
            val message = "You must be connected to perform this action"
            onShowFirebaseSnackbar(Status[Status.State.INVALID], message)
        }
    }

    fun addFridgeItem(expirationDate: String){
        val currentUser: FirebaseUser? = mAuth.currentUser
        if(currentUser != null){ // user is connected
            DateChain(expirationDate)
                .onSuccess { addProductInFridge(currentUser, expirationDate) }
                .onFailure {
                    val message = when(it){
                        is YearChainLink -> "Invalid year, must be >= 2000"
                        is MonthChainLink -> "Invalid month, must be > 0 and <= 12"
                        is DayChainLink -> "Invalid day for the given year and month"
                        else -> "Invalid"
                    }
                    onShowFirebaseSnackbar(Status[Status.State.INVALID], message)
                }.run()
        } else{ // user is not connected
            val message = "You must be connected to perform this action"
            onShowFirebaseSnackbar(Status[Status.State.INVALID], message)
        }
    }

    fun onShowFirebaseSnackbar(status: Status, message: String){
        _onShowFirebaseSnackbar.value = status with message
    }

    fun onChangeFavoriteIcon(iconId: Int){
        _onChangeFavoriteIcon.value = iconId
    }

    fun onChangeFridgeIcon(iconId: Int){
        _onChangeFridgeIcon.value = iconId
    }

    fun onMakeUnclickableFavoriteIcon(){
        _onMakeUnclickableFavoriteIcon.value = true
    }

    fun onMakeUnclickableFridgeIcon(){
        _onMakeUnclickableFridgeIcon.value = true
    }

    fun doneMakingUnclickableFavoriteIcon(){
        _onMakeUnclickableFavoriteIcon.value = false
    }

    fun doneMakingUnclickableFridgeIcon(){
        _onMakeUnclickableFridgeIcon.value = false
    }

    fun doneOpeningPopupWindow(){
        _onOpenPopupWindow.value = false
    }

    private fun loadProductInformations(){
        viewModelScope.launch {
            _status.value = LoadingStatus.LOADING
            try{
                _rawFoodProduct = foodProductRepository.get(barcode).asDomainModel()
                _foodProduct.value = _rawFoodProduct
                _status.value = LoadingStatus.DONE
            } catch(e: Exception){
                _status.value = LoadingStatus.ERROR
            }
        }
    }

    private fun checkProductInFavorites(user: FirebaseUser?){
        user?.let {
            Firebase.firestore.collection("favorites")
                .whereEqualTo("user", it.uid)
                .whereEqualTo("barcode", barcode)
                .get()
                .addOnSuccessListener { query ->
                    if(!query.isEmpty){
                        onChangeFavoriteIcon(R.drawable.ic_baseline_favorite_red_24)
                        onMakeUnclickableFavoriteIcon()
                    }
                }
        }
    }

    private fun checkProductInFridge(user: FirebaseUser?){
        user?.let {
            Firebase.firestore.collection("fridges")
                .whereEqualTo("user", it.uid)
                .whereEqualTo("barcode", barcode)
                .get()
                .addOnSuccessListener { query ->
                    if(!query.isEmpty){
                        onChangeFridgeIcon(R.drawable.ic_baseline_blue_door_front_24)
                        onMakeUnclickableFridgeIcon()
                    }
                }
        }
    }

    private fun addProductInFavorites(user: FirebaseUser){
        Firebase.firestore.collection("favorites").document().set(
            mapOf(
                "barcode" to barcode,
                "user" to user.uid,
                "image_url" to _rawFoodProduct.detail?.image,
                "product_title" to _rawFoodProduct.detail?.name,
            )
        ).addOnSuccessListener {
            val message = "Product successfully added to your favorites !"
            onShowFirebaseSnackbar(Status[Status.State.VALID], message)
            onChangeFavoriteIcon(R.drawable.ic_baseline_favorite_red_24)
            onMakeUnclickableFavoriteIcon()
        }.addOnFailureListener { exc ->
            val message = exc.message
            val finalMessage: String = message ?: "An error occurred"
            onShowFirebaseSnackbar(Status[Status.State.VALID], finalMessage)
        }
    }

    private fun addProductInFridge(user: FirebaseUser, expirationDate: String){
        Firebase.firestore.collection("fridges").document().set(
            mapOf(
                "barcode" to barcode,
                "user" to user.uid,
                "image_url" to _rawFoodProduct.detail?.image,
                "product_title" to _rawFoodProduct.detail?.name,
                "expiration_date" to expirationDate,
            )
        ).addOnSuccessListener {
            val message = "Product successfully added to your fridge !"
            onShowFirebaseSnackbar(Status[Status.State.VALID], message)
            onChangeFridgeIcon(R.drawable.ic_baseline_blue_door_front_24)
            onMakeUnclickableFridgeIcon()
        }.addOnFailureListener { exc ->
            val message = exc.message
            val finalMessage: String = message ?: "An error occurred"
            onShowFirebaseSnackbar(Status[Status.State.VALID], finalMessage)
        }
    }
}