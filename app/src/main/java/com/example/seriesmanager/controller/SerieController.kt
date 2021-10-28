package com.example.seriesmanager.controller

import com.example.seriesmanager.model.ManagerDAO
import com.example.seriesmanager.model.ManagerSqlite
import com.example.seriesmanager.model.Serie
import com.example.seriesmanager.view.SerieListaActivity

class SerieController (mainActivity: SerieListaActivity) {
    private val managerDAO: ManagerDAO = ManagerSqlite(mainActivity)

    //SÉRIES
    fun inserirSerie(serie: Serie) = managerDAO.criarSerie(serie)
    fun buscarSerie(nome: String) = managerDAO.recuperarSerie(nome)
    fun buscarSeries() = managerDAO.recuperarSeries()
    fun modificarSerie(serie: Serie) = managerDAO.atualizarSerie(serie)
    fun apagarSerie(nome: String) = managerDAO.removerSerie(nome)

}