package com.mobg5.g56080.foodee

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.firebase.auth.FirebaseAuth
import com.mobg5.g56080.foodee.databinding.ActivityMainBinding
import com.mobg5.g56080.foodee.util.Utils

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: MainViewModel
    private lateinit var mAuth: FirebaseAuth
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        setupBinding()
        setupViewModel()
        setupFirebase()
        setupNavigationController()
        setupListeners()
        setupObservers()
    }

    /* setup methods */
    private fun setupBinding(){
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    private fun setupViewModel(){
        val viewModelFactory = MainViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        binding.mainViewModel = viewModel
    }

    private fun setupFirebase(){
        mAuth = FirebaseAuth.getInstance()
        mAuth.addAuthStateListener(viewModel)
    }

    private fun setupNavigationController(){
        navController = this.findNavController(R.id.navHostFragment)
        drawerLayout = binding.drawerLayout
    }

    private fun setupListeners(){
        // menu item in application bar listener
        binding.bottomAppBar.setOnMenuItemClickListener { item ->
            viewModel.destinationTo(item.itemId)?.let { navController.navigate(it) }
            viewModel.doneMovingOutOfHomePage(); true
        }

        // navigation button in application bar listener
        binding.bottomAppBar.setNavigationOnClickListener{
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // navigation item in navigation drawer listener
        binding.navView.setNavigationItemSelectedListener { item ->
            viewModel.destinationTo(item.itemId)?.let { navController.navigate(it) }
            drawerLayout.closeDrawer(GravityCompat.START)
            item.isChecked = true
            viewModel.doneMovingOutOfHomePage(); true
        }
    }

    private fun setupObservers(){
        viewModel.moveToHomePage.observe(this){
            if(it){
                navController.navigate(R.id.homeFragment)
                viewModel.doneMovingToHomePage()
            }
        }

        viewModel.openCamera.observe(this){
            if(it){
                binding.bottomAppBar.performHide()
                binding.fab.hide()
                navController.navigate(R.id.cameraFragment)
                viewModel.doneOpeningCamera()
            }
        }

        viewModel.changeFabIcon.observe(this){
            val drawable = ResourcesCompat.getDrawable(resources, it, this.theme)
            binding.fab.setImageDrawable(drawable)
        }

        viewModel.changeLoginIcon.observe(this){ drawable ->
            val menuItem = binding.bottomAppBar.menu.children.filter { it.itemId == R.id.login }.elementAt(0)
            menuItem.icon = ResourcesCompat.getDrawable(resources, drawable, this.theme)
        }
    }

    /* lifecycle methods */
    override fun onBackPressed(){}

    override fun onDestroy(){
        super.onDestroy()
        mAuth.signOut()
    }

    override fun onResume() {
        super.onResume()
        if(!Utils.allPermissionsGranted(this)){
            Utils.requestRuntimePermissions(this)
        }
    }
}