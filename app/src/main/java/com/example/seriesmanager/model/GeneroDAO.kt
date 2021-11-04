package com.example.seriesmanager.model

interface GeneroDAO {
    fun criarGenero(genero: Genero): Long
    fun recuperarGeneros(): MutableList<String>
}