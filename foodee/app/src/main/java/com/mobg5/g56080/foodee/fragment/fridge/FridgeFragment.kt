package com.mobg5.g56080.foodee.fragment.fridge

import android.content.Context
import com.mobg5.g56080.foodee.fragment.favorites.FavoriteViewModelFactory

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.mobg5.g56080.foodee.R
import com.mobg5.g56080.foodee.databinding.FragmentFridgeBinding
import com.mobg5.g56080.foodee.fragment.favorites.FavoriteFragmentDirections
import com.mobg5.g56080.foodee.util.Utils
import kotlinx.android.synthetic.main.popup_design.view.*

class FridgeFragment: Fragment(){

    private lateinit var viewModel: FridgeViewModel
    private lateinit var binding: FragmentFridgeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        setupBinding(inflater)
        setupViewModel()
        setupRecycleView()
        setupObservers()

        return binding.root
    }

    private fun setupBinding(inflater: LayoutInflater){
        binding = FragmentFridgeBinding.inflate(inflater)
    }

    private fun setupViewModel(){
        val viewModelFactory = FridgeViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory)[FridgeViewModel::class.java]
        binding.lifecycleOwner = viewLifecycleOwner
        binding.fridgeViewModel = viewModel
    }

    private fun setupRecycleView(){
        binding.fridgeRecyclerView.adapter = FridgeAdapter(viewModel)
    }

    private fun setupObservers(){
        viewModel.onShowProduct.observe(viewLifecycleOwner){
            findNavController().navigate(FridgeFragmentDirections.actionFridgeFragmentToProductDetailFragment(it.barcode))
        }

        viewModel.onShowFirebaseSnackbar.observe(viewLifecycleOwner){ status ->
            displaySnackbar(status.customMessage!!, status.state.color)
        }

        viewModel.showFallbackMessage.observe(viewLifecycleOwner){
            binding.fridgeFallbackText.visibility = it
        }

        viewModel.onOpenPopupWindow.observe(viewLifecycleOwner){
            val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val popupView = inflater.inflate(R.layout.popup_design, null)
            val wid = LinearLayout.LayoutParams.WRAP_CONTENT
            val high = LinearLayout.LayoutParams.WRAP_CONTENT
            val focus = true
            val popupWindow = PopupWindow(popupView, wid, high, focus)

            popupView.expDateBtn.setOnClickListener { _ ->
                val text = popupView.inputExpirationDate.text
                viewModel.updateFridgeItem(it, text.toString())
                popupWindow.dismiss()
            }

            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
        }
    }

    private fun displaySnackbar(message: String, color: Int) =
        Utils.displaySnackbar(binding.fridgeLayout, message, resources, color, requireContext().theme)
}