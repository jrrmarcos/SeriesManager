package com.example.seriesmanager.controller

import com.example.seriesmanager.model.Episodio
import com.example.seriesmanager.view.EpisodioListaActivity

class EpisodioController(episodiosActivity: EpisodioListaActivity) {
    private val managerDAO: ManagerDAO = ManagerSqlite(episodiosActivity)

    //EPISÓDIOS
    fun inserirEpisodio(episodio: Episodio) = managerDAO.criarEpisodio(episodio)
    fun buscarEpisodio(numero: String) = managerDAO.recuperarEpisodio(numero)
    fun buscarEpisodio() = managerDAO.recuperarEpisodio()
    fun modificarEpisodio(episodio: Episodio) = managerDAO.atualizarEpisodio(episodio)
    fun apagarEpisodio(numero: String) = managerDAO.removerEpisodio(numero)
}