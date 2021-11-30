package com.example.seriesmanager.controller

import com.example.seriesmanager.model.*
import com.example.seriesmanager.view.EpisodioListaActivity

class EpisodioController (temporada: Temporada) {
    //private val episodioDAO: EpisodioDAO = EpisodioSqlite(episodiosListaActivity)
    private val episodioDAO: EpisodioDAO = EpisodioFirebase(temporada)

    fun inserirEpisodio(episodio: Episodio) = episodioDAO.criarEpisodio(episodio)
    fun buscarEpisodios(temporadaId: Int) = episodioDAO.recuperarEpisodios(temporadaId)
    fun buscarEpisodios() = episodioDAO.recuperarEpisodios()
    fun modificarEpisodio(episodio: Episodio) = episodioDAO.atualizarEpisodio(episodio)
    fun apagarEpisodio(temporadaId: Int, numeroSequencial: Int) = episodioDAO.removerEpisodio(temporadaId, numeroSequencial)
    fun apagarEpisodio(nomeEpisodio: String, numeroSequencial: Int) = episodioDAO.removerEpisodio(nomeEpisodio, numeroSequencial)
}