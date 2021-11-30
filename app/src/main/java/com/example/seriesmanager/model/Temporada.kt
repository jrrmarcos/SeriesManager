package com.example.seriesmanager.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Temporada(
    val numeroSequencialTemp: Int = -9999,
    val anoLancamentoTemp: String = "",
    val qtdEpisodiosTemp: String = "",
    val nomeSerie: String = ""
): Parcelable