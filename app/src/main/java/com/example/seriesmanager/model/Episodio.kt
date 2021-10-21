package com.example.seriesmanager.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Episodio (
        val numeroSequencial: Int,
        val nome: String = "",
        val tempoDuracao: String = ""
        ): Parcelable
