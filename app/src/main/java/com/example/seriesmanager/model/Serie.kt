package com.example.seriesmanager.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Serie(
    val nomeSerie: String = "",
    val anoLancamentoSerie: String = "",
    val emissoraSerie: String = "",
    val generoSerie: String = ""
): Parcelable