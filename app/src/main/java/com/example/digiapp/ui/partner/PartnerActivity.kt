package com.example.digiapp.ui.partner

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.digiapp.R
import com.example.digiapp.databinding.ActivityPartnerBinding

class PartnerActivity : AppCompatActivity() {

    //init binding
    private lateinit var binding: ActivityPartnerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPartnerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }


    private fun initUI(){
        initStatusBarColor()
    }

    private fun initStatusBarColor(){
        window.statusBarColor = ContextCompat.getColor(this, R.color.partner_bg)

    }

    private fun initListeners(){

    }

}