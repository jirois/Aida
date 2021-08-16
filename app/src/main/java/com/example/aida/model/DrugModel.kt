package com.example.aida.model

import java.io.Serializable

data class DrugModel(
    val id: Int,
    val drugName: String,
    val drugPrice: String,
    val drugDate: String,
    val drugImage: String,
    val pharmacyName: String,
    val pharmacyLocation: String,
    val latitude: Double,
    val longitude: Double
):Serializable
