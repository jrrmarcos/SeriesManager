package com.example.seriesmanager.model

interface EpisodioDAO {
    fun criarEpisodio(episodio: Episodio): Long
    fun recuperarEpisodio(nome: String): Episodio
    fun recuperarEpisodio(): MutableList<Episodio>
    fun atualizarEpisodio(episodio: Episodio): Int
    fun removerEpisodio(nome: String): Int
}