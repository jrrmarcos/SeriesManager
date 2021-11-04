package com.example.seriesmanager.model

interface SerieDAO {
    fun criarSerie(serie: Serie): Long
    fun recuperarSeries(): MutableList<Serie>
    fun removerSerie(nome: String): Int
}