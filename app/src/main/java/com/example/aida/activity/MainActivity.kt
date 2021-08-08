package com.example.aida.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aida.R
import com.example.aida.adapters.DrugAdapter
import com.example.aida.database.DatabaseHandler
import com.example.aida.databinding.ActivityMainBinding
import com.example.aida.model.DrugModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.fabAddHappyPlace.setOnClickListener {
            startActivityForResult(Intent(this@MainActivity, AddDrugInfo::class.java), ADD_AIDA_DRUG_REQUEST)
        }
        binding.ivAdd.setOnClickListener {
            startActivityForResult(Intent(this@MainActivity, AddDrugInfo::class.java), ADD_AIDA_DRUG_REQUEST)
        }

        getAidaDrugListFromLocalDB()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_AIDA_DRUG_REQUEST) {
            if (resultCode == Activity.RESULT_OK){
                getAidaDrugListFromLocalDB()
            } else if ( resultCode == Activity.RESULT_CANCELED){
                Log.e("Activity", "Cancelled or Backpressed ")
            }
        }
    }

    private fun getAidaDrugListFromLocalDB(){
        val dbHandler = DatabaseHandler(this)

        val getAidaDrugList = dbHandler.getAidaDrug()

        if (getAidaDrugList.size > 0) {
            binding.rvAidaDrug.visibility = View.VISIBLE
            binding.ivAdd.visibility = View.GONE
            binding.tvNoAvailtext.visibility = View.GONE
            setupDrugRecyclerview(getAidaDrugList)
        } else {
            binding.rvAidaDrug.visibility = View.GONE
            binding.ivAdd.visibility = View.VISIBLE
            binding.tvNoAvailtext.visibility = View.VISIBLE
        }
    }

    private fun setupDrugRecyclerview(drugList: ArrayList<DrugModel>){
        binding.rvAidaDrug.layoutManager = LinearLayoutManager(this)
        binding.rvAidaDrug.setHasFixedSize(true)

        val drugAdapter = DrugAdapter(this, drugList)
        binding.rvAidaDrug.adapter = drugAdapter

    }


    companion object {
        private const val ADD_AIDA_DRUG_REQUEST = 1
    }
}
