package com.example.gaztest.data.city

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class City(
    val id: String,
    val city: String = " ",
    val latitude: String,
    val longitude: String
) : Parcelable