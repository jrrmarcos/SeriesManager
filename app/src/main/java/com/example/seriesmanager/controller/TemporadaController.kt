package com.example.seriesmanager.controller

import com.example.seriesmanager.model.Temporada
import com.example.seriesmanager.view.TemporadaListaActivity

class TemporadaController(temporadasActivity: TemporadaListaActivity) {
    private val managerDAO: ManagerDAO = ManagerSqlite(temporadasActivity)

    //TEMPORADAS
    fun inserirTemporada(temporada: Temporada) = managerDAO.criarTemporada(temporada)
    fun buscarTemporada(nomeSerie: String, numero: String) = managerDAO.recuperarTemporada(nomeSerie,numero)
    fun buscarTemporadas(nomeSerie: String) = managerDAO.recuperarTemporadas(nomeSerie)
    //fun buscarTemporadas() = managerDAO.recuperarTemporadas()
    fun modificarTemporada(temporada: Temporada) = managerDAO.atualizarTemporada(temporada)
    fun apagarTemporada(numero: String) = managerDAO.removerTemporada(numero)
}