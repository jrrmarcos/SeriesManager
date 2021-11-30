package com.example.seriesmanager.controller

import com.example.seriesmanager.model.*
import com.example.seriesmanager.view.TemporadaListaActivity

class TemporadaController(serie: Serie) {
    //private val temporadaDAO: TemporadaDAO = TemporadaSqlite(temporadasListaActivity)
    private val temporadaDAO: TemporadaDAO = TemporadaFirebase(serie)

    fun inserirTemporada(temporada: Temporada) = temporadaDAO.criarTemporada(temporada)
    fun buscarTemporadas(nomeSerie: String) = temporadaDAO.recuperarTemporadas(nomeSerie)
    fun apagarTemporadas(nomeSerie: String, numeroSequencial: Int) = temporadaDAO.removerTemporada(nomeSerie, numeroSequencial)
    fun buscarTemporadaId(nomeSerie: String, numeroSequencial: Int) = temporadaDAO.buscarTemporadaId(nomeSerie, numeroSequencial)

}