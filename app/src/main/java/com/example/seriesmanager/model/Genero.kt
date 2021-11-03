package com.example.seriesmanager.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Genero(
    val nome: String = ""
): Parcelable