package com.mobg5.g56080.foodee.fragment.foodproduct

import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.core.content.getSystemService
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.androidadvance.topsnackbar.TSnackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mobg5.g56080.foodee.MainActivity
import com.mobg5.g56080.foodee.R
import com.mobg5.g56080.foodee.database.ApplicationDatabase
import com.mobg5.g56080.foodee.database.foodproduct.FoodProductRepository
import com.mobg5.g56080.foodee.databinding.FragmentProductDetailBinding
import com.mobg5.g56080.foodee.util.Utils
import com.mobg5.g56080.foodee.util.status.Status
import kotlinx.android.synthetic.main.popup_design.view.*

class FoodProductFragment: Fragment() {

    private lateinit var viewModel: FoodProductViewModel
    private lateinit var binding: FragmentProductDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View{

        setupBinding(inflater)
        setupViewModel()
        setupRecycleView()
        setupObservers()

        return binding.root
    }

    private fun setupBinding(inflater: LayoutInflater){
        binding = FragmentProductDetailBinding.inflate(inflater)
    }

    private fun setupViewModel(){
        val application = requireNotNull(this.activity).application
        val args = FoodProductFragmentArgs.fromBundle(requireArguments())
        val dataSource = ApplicationDatabase.getInstance(application).foodProductDao
        val repository = FoodProductRepository(dataSource)
        val viewModelFactory = FoodProductViewModelFactory(repository, args.barcode)

        viewModel = ViewModelProvider(this, viewModelFactory)[FoodProductViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupRecycleView(){
        binding.recyclerView.adapter = FoodProductAdapter()
    }

    private fun setupObservers(){
        viewModel.onShowFirebaseSnackbar.observe(viewLifecycleOwner){status ->
            displaySnackbar(status.customMessage!!, status.state.color)
        }

        viewModel.onChangeFavoriteIcon.observe(viewLifecycleOwner){
            val drawable = ResourcesCompat.getDrawable(
                resources,
                it,
                requireContext().theme)

            binding.favoriteImg.setImageDrawable(drawable)
        }

        viewModel.onChangeFridgeIcon.observe(viewLifecycleOwner){
            val drawable = ResourcesCompat.getDrawable(
                resources,
                it,
                requireContext().theme)

            binding.fridgeImg.setImageDrawable(drawable)
        }

        viewModel.onMakeUnclickableFavoriteIcon.observe(viewLifecycleOwner){
            if(it){
                binding.favoriteImg.isClickable = false
                viewModel.doneMakingUnclickableFavoriteIcon()
            }
        }

        viewModel.onMakeUnclickableFridgeIcon.observe(viewLifecycleOwner){
            if(it){
                binding.fridgeImg.isClickable = false
                viewModel.doneMakingUnclickableFridgeIcon()
            }
        }

        viewModel.onOpenPopupWindow.observe(viewLifecycleOwner){
            if(it){
                val inflater = requireContext().getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val popupView = inflater.inflate(R.layout.popup_design, null)
                val wid = LinearLayout.LayoutParams.WRAP_CONTENT
                val high = LinearLayout.LayoutParams.WRAP_CONTENT
                val focus = true
                val popupWindow = PopupWindow(popupView, wid, high, focus)

                popupView.expDateBtn.setOnClickListener {
                    val text = popupView.inputExpirationDate.text
                    viewModel.addFridgeItem(text.toString())
                    popupWindow.dismiss()
                }

                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
                viewModel.doneOpeningPopupWindow()
            }
        }
    }

    private fun displaySnackbar(message: String, color: Int) =
        Utils.displaySnackbar(binding.productLayout, message, resources, color, requireContext().theme)
}
