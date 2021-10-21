package com.example.seriesmanager.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Serie (
    val nome: String = "", //chave primária
    val ano_lancamento: String = "",
    val emissora: String="",
    val genero: String=""
    ) : Parcelable