package com.example.seriesmanager.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Episodio (
        val numeroSequencialEp: String = "",
        val nomeEp: String = "",
        val tempoDuracaoEp: String = "",
        //var assistidoEp: Boolean = false
): Parcelable
