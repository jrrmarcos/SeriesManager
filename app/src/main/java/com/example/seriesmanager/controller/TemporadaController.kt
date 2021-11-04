package com.example.seriesmanager.controller

import com.example.seriesmanager.model.Temporada
import com.example.seriesmanager.model.TemporadaDAO
import com.example.seriesmanager.model.TemporadaSqlite
import com.example.seriesmanager.view.TemporadaListaActivity

class TemporadaController(temporadasListaActivity: TemporadaListaActivity) {
    private val temporadaDAO: TemporadaDAO = TemporadaSqlite(temporadasListaActivity)

    fun inserirTemporada(temporada: Temporada) = temporadaDAO.criarTemporada(temporada)
    fun buscarTemporadas(nomeSerie: String) = temporadaDAO.recuperarTemporadas(nomeSerie)
    fun apagarTemporadas(nomeSerie: String, numeroSequencial: Int) = temporadaDAO.removerTemporada(nomeSerie, numeroSequencial)
    fun buscarTemporadaId(nomeSerie: String, numeroSequencial: Int) = temporadaDAO.buscarTemporadaId(nomeSerie, numeroSequencial)

}