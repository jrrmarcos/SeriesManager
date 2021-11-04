package com.example.seriesmanager.controller

import com.example.seriesmanager.model.Episodio
import com.example.seriesmanager.model.EpisodioDAO
import com.example.seriesmanager.model.EpisodioSqlite
import com.example.seriesmanager.view.EpisodioListaActivity

class EpisodioController (episodiosListaActivity: EpisodioListaActivity) {
    private val episodioDAO: EpisodioDAO = EpisodioSqlite(episodiosListaActivity)

    fun inserirEpisodio(episodio: Episodio) = episodioDAO.criarEpisodio(episodio)
    fun buscarEpisodios(temporadaId: Int) = episodioDAO.recuperarEpisodios(temporadaId)
    fun modificarEpisodio(episodio: Episodio) = episodioDAO.atualizarEpisodio(episodio)
    fun apagarEpisodio(temporadaId: Int, numeroSequencial: Int) = episodioDAO.removerEpisodio(temporadaId, numeroSequencial)
}