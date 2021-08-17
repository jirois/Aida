package com.example.aida.activity

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.aida.R
import com.example.aida.databinding.ActivityAidaDrugDetailsBinding
import com.example.aida.model.DrugModel

class AidaDrugDetails : AppCompatActivity() {
    lateinit var binding: ActivityAidaDrugDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding =DataBindingUtil.setContentView(this, R.layout.activity_aida_drug_details)


        var aidaDrugDetailModel : DrugModel? = null

        if (intent.hasExtra(MainActivity.EXTRA_DRUG_DETAILS)){
            aidaDrugDetailModel =
                intent.getSerializableExtra(MainActivity.EXTRA_DRUG_DETAILS) as DrugModel
        }

        if (aidaDrugDetailModel != null) {
            setSupportActionBar(binding.toolbarAidaDrugDetail)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = aidaDrugDetailModel.drugName

            binding.toolbarAidaDrugDetail.setNavigationOnClickListener {
                onBackPressed()
            }
            binding.ivImageDetail.setImageURI(Uri.parse(aidaDrugDetailModel.drugImage))
            binding.tvDrugNameDetail.text = aidaDrugDetailModel.drugName
            binding.tvExpireDateDetail.text = aidaDrugDetailModel.drugDate
            binding.tvPharmacyDetail.text = aidaDrugDetailModel.pharmacyName
            binding.tvRxaddressDetail.text = aidaDrugDetailModel.pharmacyLocation
            binding.tvPriceDetail.text = aidaDrugDetailModel.drugPrice


        }

    }
}