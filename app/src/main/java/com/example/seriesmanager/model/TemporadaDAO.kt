package com.example.seriesmanager.model

interface TemporadaDAO {
    fun criarTemporada(temporada: Temporada): Long
    fun recuperarTemporadas(nomeSerie: String): MutableList<Temporada>
    fun removerTemporada(nomeSerie: String, numeroSequencial: Int): Int
    fun buscarTemporadaId(nomeSerie: String, numeroSequencial: Int): Int
}