package com.example.aida.activity

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.aida.R
import com.example.aida.database.DatabaseHandler
import com.example.aida.databinding.ActivityAddDrugInfoBinding
import com.example.aida.model.DrugModel
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

class AddDrugInfo : AppCompatActivity() {
    lateinit var binding: ActivityAddDrugInfoBinding
    private var cal = Calendar.getInstance()
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener

    private var saveImageToInternalStorage: Uri? = null
    private var mLatitude: Double = 0.0
    private var mLongitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_drug_info)

        setSupportActionBar(binding.toolbarAddDrug)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarAddDrug.setNavigationOnClickListener {
            onBackPressed()
        }


        dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
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

        binding.tvAddImage.setOnClickListener {
            val pictureDialog = androidx.appcompat.app.AlertDialog.Builder(this)
            pictureDialog.setTitle("Select Action")
            val pictureDialogItems =
                arrayOf("Select photo from gallery", "Capture photo from camera")
            pictureDialog.setItems(pictureDialogItems) { _, which ->
                when (which) {
                    0 -> choosePhotoFromGallery()
                    1 -> takePhotoFromCamera()
                }
            }
            pictureDialog.show()
        }

        binding.btnSave.setOnClickListener {
            when {
                binding.etDrugName.text.isNullOrEmpty() -> {
                    Toast.makeText(this, "Please enter drug name", Toast.LENGTH_SHORT).show()
                }
                binding.etDrugPrice.text.isNullOrEmpty() -> {
                    Toast.makeText(this, "Please enter drug price", Toast.LENGTH_SHORT).show()
                }
                binding.etExpirationDate.text.isNullOrEmpty() -> {
                    Toast.makeText(this, "Please enter drug expiration date", Toast.LENGTH_SHORT)
                        .show()
                }
                binding.etPharmacy.text.isNullOrEmpty() -> {
                    Toast.makeText(this, "Please enter pharmacy name", Toast.LENGTH_SHORT).show()
                }

                binding.etLocation.text.isNullOrEmpty() -> {
                    Toast.makeText(this, "Please enter location", Toast.LENGTH_SHORT).show()

                }
                saveImageToInternalStorage == null -> {

                    Toast.makeText(this, "Please enter image", Toast.LENGTH_SHORT).show()

                }
                else -> {
                    val drugModel = DrugModel(
                        0,
                        binding.etDrugName.text.toString(),
                        binding.etDrugPrice.text.toString(),
                        binding.etExpirationDate.text.toString(),
                        saveImageToInternalStorage.toString(),
                        binding.etPharmacy.text.toString(),
                        binding.etLocation.text.toString(),
                        mLatitude,
                        mLongitude
                    )

                    // initialize database
                    val dbHandler = DatabaseHandler(this)

                    val aidaDrugs = dbHandler.addAidaDrug(drugModel)

                    if (aidaDrugs > 0) {
                        setResult(Activity.RESULT_OK)
                        finish()
                    }

                }
            }


        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == GALLERY) {
                if (data != null){
                    val contentURI = data.data
                    try {
                        val selectedImageBitmap =
                            MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)

                        saveImageToInternalStorage = saveImageToInternalStorage(selectedImageBitmap)
                        Log.e("Save Image: ", "Path :: $saveImageToInternalStorage")


                        binding.ivPlaceImage!!.setImageBitmap(selectedImageBitmap)
                    } catch (e: IOException) {
                        e.printStackTrace()

                        Toast.makeText(this, "Fail to upload image", Toast.LENGTH_SHORT).show()
                    }
                }
            } else if (requestCode == CAMERA){
                val thumbnail: Bitmap = data!!.extras!!.get("data") as Bitmap

                saveImageToInternalStorage = saveImageToInternalStorage(thumbnail)
                Log.e("Save Image: ", "Path :: $saveImageToInternalStorage")


                binding.ivPlaceImage!!.setImageBitmap(thumbnail)
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.e("Cancelled", "Cancel")
        }
    }




    private fun updateDateInView() {
        val myFormat = "d MMM, yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        binding.etExpirationDate.setText(sdf.format(cal.time).toString())
    }

    private fun choosePhotoFromGallery(){
        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object: MultiplePermissionsListener{
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report!!.areAllPermissionsGranted()){
                        val galleryIntent = Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        )
                        startActivityForResult(galleryIntent, GALLERY)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    showRationalDialogForPermission()
                }

            }).onSameThread()
            .check()
    }

    private fun takePhotoFromCamera(){

        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
            .withListener(object: MultiplePermissionsListener{
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report!!.areAllPermissionsGranted()){
                        val galleryIntent = Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE
                        )
                        startActivityForResult(galleryIntent, CAMERA)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    showRationalDialogForPermission()
                }

            }).onSameThread()
            .check()
    }


    private fun showRationalDialogForPermission(){
        AlertDialog.Builder(this)
            .setMessage("It Looks like you have turned off permissions required for this feature. It can be enabled under Application Settings")
            .setPositiveButton("GO TO SETTING"){
                _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton("Cancel"){
                dialog, _ ->
                dialog.dismiss()
            }.show()

    }

    private fun saveImageToInternalStorage(bitmap: Bitmap): Uri{

        val wrapper = ContextWrapper(applicationContext)

        var file = wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE)

        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            val stream: OutputStream = FileOutputStream(file)

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)

            stream.flush()

            stream.close()
        } catch (e: IOException){
            e.printStackTrace()
        }
        return Uri.parse(file.absolutePath)
    }

    companion object{
        private const val GALLERY = 1

        private const val CAMERA = 2

        private const val IMAGE_DIRECTORY = "AidaDrugMarket"
    }
}