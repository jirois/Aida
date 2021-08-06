package com.example.aida.model

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
)
