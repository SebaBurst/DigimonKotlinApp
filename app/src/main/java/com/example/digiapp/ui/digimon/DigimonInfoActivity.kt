package com.example.digiapp.ui.digimon

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.digiapp.R
import com.example.digiapp.databinding.ActivityDigimonInfoBinding
import com.example.digiapp.ui.partner.PartnerActivity

class DigimonInfoActivity : AppCompatActivity() {

    //add view binding here

    private lateinit var binding: ActivityDigimonInfoBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDigimonInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
        initListeners()
    }

    /**
     * Initialize the UI components
     */
    private fun initUI(){
        initStatusBarColor()
    }

    private fun initStatusBarColor(){
        window.statusBarColor = ContextCompat.getColor(this, R.color.digi_info_bg)

    }

    /**
     * Initialize the listeners
     */
    private fun initListeners(){
        binding.partnerLayout.setOnClickListener {
            goToPartnerInfo()
        }
    }


    /**
     * Go to the partner info activity
     */
    private fun goToPartnerInfo(){
        val intent = Intent(this, PartnerActivity::class.java);
        startActivity(intent)
    }
}