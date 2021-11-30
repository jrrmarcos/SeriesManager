package com.example.seriesmanager.model

interface EpisodioDAO {
    fun criarEpisodio(episodio: Episodio): Long
    fun recuperarEpisodios(temporadaId: Int): MutableList<Episodio>
    fun recuperarEpisodios(): MutableList<Episodio>
    fun recuperarEpisodio(numeroSequencial: Int, temporadaId: Int): Episodio?
    fun atualizarEpisodio(episodio: Episodio): Int
    fun removerEpisodio(temporadaId: Int, numeroSequencial: Int): Int
    fun removerEpisodio(nomeEpisodio: String, numeroSequencial: Int): Int
}