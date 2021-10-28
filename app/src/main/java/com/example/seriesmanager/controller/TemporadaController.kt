package com.example.seriesmanager.controller

import com.example.seriesmanager.model.ManagerDAO
import com.example.seriesmanager.model.ManagerSqlite
import com.example.seriesmanager.model.Temporada
import com.example.seriesmanager.view.TemporadaListaActivity

class TemporadaController(temporadasActivity: TemporadaListaActivity) {
    private val managerDAO: ManagerDAO = ManagerSqlite(temporadasActivity)

    //TEMPORADAS
    fun inserirTemporada(temporada: Temporada) = managerDAO.criarTemporada(temporada)
    fun buscarTemporada(numero: String) = managerDAO.recuperarTemporada(numero)
    fun buscarTemporadas() = managerDAO.recuperarTemporada()
    fun modificarTemporada(temporada: Temporada) = managerDAO.atualizarTemporada(temporada)
    fun apagarTemporada(numero: String) = managerDAO.removerTemporada(numero)
}