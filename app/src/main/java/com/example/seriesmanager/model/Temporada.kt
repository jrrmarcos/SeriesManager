package com.example.seriesmanager.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Temporada(
    val numeroSequencialTemp: String = "",
    val qtdEpisodiosTemp: String = "",
    val anoLancamentoTemp: String = ""
): Parcelable