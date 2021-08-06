package com.example.aida.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aida.R
import com.example.aida.adapters.DrugAdapter
import com.example.aida.databinding.ActivityMainBinding
import com.example.aida.model.DrugModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var drugModelList = ArrayList<DrugModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.fabAddHappyPlace.setOnClickListener {
            startActivity(Intent(this@MainActivity, AddDrugInfo::class.java))
        }
        binding.ivAdd.setOnClickListener {
            startActivity(Intent(this@MainActivity, AddDrugInfo::class.java))
        }

        setupDrugRecyclerview(drugModelList)
    }

    private fun setupDrugRecyclerview(drugList: ArrayList<DrugModel>){
        binding.rvAidaDrug.layoutManager = LinearLayoutManager(this)
        binding.rvAidaDrug.setHasFixedSize(true)

        val drugAdapter = DrugAdapter(this, drugList)
        binding.rvAidaDrug.adapter = drugAdapter

    }
}
