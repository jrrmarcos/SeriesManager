package com.example.seriesmanager.controller

import com.example.seriesmanager.model.Genero
import com.example.seriesmanager.model.GeneroDAO
import com.example.seriesmanager.model.GeneroSqlite
import com.example.seriesmanager.view.SerieActivity

class GeneroController (seriesActivity: SerieActivity) {

    private val generoDAO: GeneroDAO = GeneroSqlite(seriesActivity)

    fun inserirGenero(genero: Genero) = generoDAO.criarGenero(genero)
    fun buscarGeneros() = generoDAO.recuperarGeneros()
}