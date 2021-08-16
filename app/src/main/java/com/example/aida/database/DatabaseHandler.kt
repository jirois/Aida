package com.example.aida.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.aida.model.DrugModel

class DatabaseHandler (context: Context):
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "HappyPlacesDatabase"
        private const val TABLE_AIDA_DRUG = "AidaDrugTable"

        //All the columns names

        private const val KEY_ID = "_id"
        private const val KEY_DRUG_NAME = "drugName"
        private const val KEY_DRUG_PRICE = "drugPrice"
        private const val KEY_DRUG_EXPIRATION = "drugDate"
        private const val KEY_IMAGE = "drugImage"
        private const val KEY_PHARMACY_NAME = "pharmacyName"
        private const val KEY_LOCATION = "location"
        private const val KEY_LATITUDE = "latitude"
        private const val KEY_LONGITUDE = "longitude"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        //creating table with fields
        val CREATE_AIDA_DRUG_TABLE = ("CREATE TABLE "+ TABLE_AIDA_DRUG + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_DRUG_NAME + " TEXT,"
                + KEY_DRUG_PRICE + " TEXT,"
                + KEY_DRUG_EXPIRATION + " TEXT,"
                + KEY_IMAGE + " TEXT,"
                + KEY_PHARMACY_NAME + " TEXT,"
                + KEY_LOCATION + " TEXT,"
                + KEY_LATITUDE + " TEXT,"
                + KEY_LONGITUDE + " TEXT)")
        db?.execSQL(CREATE_AIDA_DRUG_TABLE)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_AIDA_DRUG")
        onCreate(db)
    }


    fun addAidaDrug(aidaDrug: DrugModel):Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_DRUG_NAME, aidaDrug.drugName)
        contentValues.put(KEY_DRUG_PRICE, aidaDrug.drugPrice)
        contentValues.put(KEY_DRUG_EXPIRATION, aidaDrug.drugDate)
        contentValues.put(KEY_IMAGE, aidaDrug.drugImage)
        contentValues.put(KEY_PHARMACY_NAME, aidaDrug.pharmacyName)
        contentValues.put(KEY_LOCATION, aidaDrug.pharmacyLocation)
        contentValues.put(KEY_LATITUDE, aidaDrug.latitude)
        contentValues.put(KEY_LONGITUDE, aidaDrug.longitude)

        // Inserting Row
        val result = db.insert(TABLE_AIDA_DRUG, null, contentValues)
        db.close()
        return result
    }


    // reading database
    fun getAidaDrug(): ArrayList<DrugModel>{

        val aidaDrugList : ArrayList<DrugModel> = ArrayList()

        val selectQuery = "SELECT * FROM $TABLE_AIDA_DRUG"

        val db = this.readableDatabase

        try {
            val cursor: Cursor = db.rawQuery(selectQuery, null)

            if (cursor.moveToFirst()){
                do{
                    val drug = DrugModel(
                        cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_DRUG_NAME)),
                        cursor.getString(cursor.getColumnIndex(KEY_DRUG_PRICE)),
                        cursor.getString(cursor.getColumnIndex(KEY_DRUG_EXPIRATION)),
                        cursor.getString(cursor.getColumnIndex(KEY_IMAGE)),
                        cursor.getString(cursor.getColumnIndex(KEY_PHARMACY_NAME)),
                        cursor.getString(cursor.getColumnIndex(KEY_LOCATION)),
                        cursor.getDouble(cursor.getColumnIndex(KEY_LATITUDE)),
                        cursor.getDouble(cursor.getColumnIndex(KEY_LONGITUDE)),
                    )
                    aidaDrugList.add(drug)
                }while (cursor.moveToNext())
            }
            cursor.close()

        }catch (e: SQLiteException){
            db.execSQL(selectQuery)
            return ArrayList()
        }
        return aidaDrugList
    }


    fun updateAidaDrug(aidaDrug: DrugModel):Int {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_DRUG_NAME, aidaDrug.drugName)
        contentValues.put(KEY_DRUG_PRICE, aidaDrug.drugPrice)
        contentValues.put(KEY_DRUG_EXPIRATION, aidaDrug.drugDate)
        contentValues.put(KEY_IMAGE, aidaDrug.drugImage)
        contentValues.put(KEY_PHARMACY_NAME, aidaDrug.pharmacyName)
        contentValues.put(KEY_LOCATION, aidaDrug.pharmacyLocation)
        contentValues.put(KEY_LATITUDE, aidaDrug.latitude)
        contentValues.put(KEY_LONGITUDE, aidaDrug.longitude)

        // Updating Row
        val success =
            db.update(TABLE_AIDA_DRUG, contentValues, KEY_ID + "=" + aidaDrug.id, null)
        db.close()
        return success
    }

}