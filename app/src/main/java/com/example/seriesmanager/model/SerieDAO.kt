package com.example.seriesmanager.model

interface SerieDAO {
    fun criarSerie(serie: Serie): Long
    fun recuperarSerie(nome: String): Serie
    fun recuperarSeries(): MutableList<Serie>
    fun atualizarSerie(serie: Serie): Int
    fun removerSerie(nome: String): Int
}