package com.example.aida

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.aida.databinding.ActivityAddDrugInfoBinding
import java.text.SimpleDateFormat
import java.util.*

class AddDrugInfo : AppCompatActivity() {
    lateinit var binding: ActivityAddDrugInfoBinding
    private var cal = Calendar.getInstance()
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_add_drug_info)

        setSupportActionBar(binding.toolbarAddDrug)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarAddDrug.setNavigationOnClickListener {
            onBackPressed()
        }


        dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            cal.set(Calendar.DAY_OF_YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            updateDateInView()
        }

        binding.etExpirationDate.setOnClickListener {
            DatePickerDialog(
                this,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }


    private fun updateDateInView() {
        val myFormat = "d MMM, yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        binding.etExpirationDate.setText(sdf.format(cal.time).toString())
    }
}