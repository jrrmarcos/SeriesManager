package com.example.seriesmanager.controller

import com.example.seriesmanager.model.Serie
import com.example.seriesmanager.model.SerieDAO
import com.example.seriesmanager.model.SerieFirebase
import com.example.seriesmanager.model.SerieSqlite
import com.example.seriesmanager.view.SerieListaActivity

class SerieController (seriesListaActivity: SerieListaActivity) {
    //private val serieDAO: SerieDAO = SerieSqlite(seriesListaActivity)
    private val serieDAO: SerieDAO = SerieFirebase()

    fun inserirSerie(serie: Serie) = serieDAO.criarSerie(serie)
    fun buscarSeries() = serieDAO.recuperarSeries()
    fun apagarSerie(nome: String) = serieDAO.removerSerie(nome)

}