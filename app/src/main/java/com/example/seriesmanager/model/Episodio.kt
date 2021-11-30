package com.example.seriesmanager.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Episodio(
        val numeroSequencialEp: Int = -1,
        val nomeEp: String = "",
        val duracaoEp: Int = -9999,
        val assistidoEp: Boolean = false,
        val temporadaId: Int = -1
): Parcelable
