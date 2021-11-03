package com.example.seriesmanager.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Episodio(
        val numeroSequencialEp: Int,
        val nomeEp: String,
        val duracaoEp: Int,
        val assistidoEp: Boolean,
        val temporadaId: Int
): Parcelable
