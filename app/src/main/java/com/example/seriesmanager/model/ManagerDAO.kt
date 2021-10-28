package com.example.seriesmanager.model

interface ManagerDAO {
    //Funções de Séries
    fun criarSerie(serie: Serie): Long
    fun recuperarSerie(nome: String): Serie
    fun recuperarSeries(): MutableList<Serie>
    fun atualizarSerie(serie: Serie): Int
    fun removerSerie(nome: String): Int

    //Funções de Temporadas
    fun criarTemporada(temporada: Temporada): Long
    fun recuperarTemporada(numero: String): Temporada
    fun recuperarTemporada(): MutableList<Temporada>
    fun atualizarTemporada(temporada: Temporada): Int
    fun removerTemporada(numero: String): Int

    //Funções de Episódios
    fun criarEpisodio(episodio: Episodio): Long
    fun recuperarEpisodio(nome: String): Episodio
    fun recuperarEpisodio(): MutableList<Episodio>
    fun atualizarEpisodio(episodio: Episodio): Int
    fun removerEpisodio(nome: String): Int

}