package com.example.seriesmanager.controller

import com.example.seriesmanager.model.Serie
import com.example.seriesmanager.model.SerieDAO
import com.example.seriesmanager.model.SerieSqlite
import com.example.seriesmanager.view.MainActivity

class SerieController(mainActivity: MainActivity) {
    private val serieDAO: SerieDAO = SerieSqlite(mainActivity)

    fun inserirSerie(serie: Serie) = serieDAO.criarSerie(serie)
    fun buscarSerie(nome: String) = serieDAO.recuperarSerie(nome)
    fun buscarSeries() = serieDAO.recuperarSeries()
    fun modificarSerie(serie: Serie) = serieDAO.atualizarSerie(serie)
    fun apagarSerie(nome: String) = serieDAO.removerSerie(nome)
}