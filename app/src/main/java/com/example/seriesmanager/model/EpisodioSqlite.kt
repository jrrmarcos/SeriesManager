package com.example.seriesmanager.model

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase

class EpisodioSqlite  (contexto: Context): EpisodioDAO {
    private val bdSeries: SQLiteDatabase = Manager(contexto).getSeriesBD()

    override fun criarEpisodio(episodio: Episodio): Long {
        val episodioCv = ContentValues()
        episodioCv.put("numero_sequencial", episodio.numeroSequencialEp)
        episodioCv.put("nome", episodio.nomeEp)
        episodioCv.put("duracao", episodio.duracaoEp)
        episodioCv.put("assistido", episodio.assistidoEp)
        episodioCv.put("temporada_id", episodio.temporadaId)

        return bdSeries.insert("EPISODIO", null, episodioCv)
    }

    override fun recuperarEpisodios(temporadaId: Int): MutableList<Episodio> {
        val episodiosList: MutableList<Episodio> = ArrayList()
        val episodioCursor = bdSeries.rawQuery("SELECT * " +
                "                                   FROM EPISODIO WHERE temporada_id = ?",
            arrayOf(temporadaId.toString()))
        val episodio: Episodio

        if (episodioCursor.moveToFirst()) {
            while (!episodioCursor.isAfterLast) {
                val episodio = Episodio(
                    episodioCursor.getInt(episodioCursor.getColumnIndexOrThrow("numero_sequencial")),
                    episodioCursor.getString(episodioCursor.getColumnIndexOrThrow("nome")),
                    episodioCursor.getInt(episodioCursor.getColumnIndexOrThrow("duracao")),
                    intToBoolean(episodioCursor.getInt(episodioCursor.getColumnIndexOrThrow("assistido"))),
                    episodioCursor.getInt(episodioCursor.getColumnIndexOrThrow("temporada_id"))
                )
                episodiosList.add(episodio)
                episodioCursor.moveToNext()
            }
        }
        return episodiosList
    }

    override fun recuperarEpisodio(numeroSequencial: Int, temporadaId: Int): Episodio? {
        var episodio: Episodio? = null
        val episodioCursor = bdSeries.rawQuery("SELECT * FROM EPISODIO " +
                "WHERE numero_sequencial = ? AND temporada_id = ?",
            arrayOf(numeroSequencial.toString(), temporadaId.toString()))

        if (episodioCursor.moveToFirst()) run {
            episodio = Episodio(
                episodioCursor.getInt(episodioCursor.getColumnIndexOrThrow("numero_sequencial")),
                episodioCursor.getString(episodioCursor.getColumnIndexOrThrow("nome")),
                episodioCursor.getInt(episodioCursor.getColumnIndexOrThrow("duracao")),
                intToBoolean(episodioCursor.getInt(episodioCursor.getColumnIndexOrThrow("assistido"))),
                episodioCursor.getInt(episodioCursor.getColumnIndexOrThrow("temporada_id"))
            )
        }
        return episodio
    }


    override fun atualizarEpisodio(episodio: Episodio): Int {
        val episodioCv = ContentValues()
        episodioCv.put("numero_sequencial", episodio.numeroSequencialEp)
        episodioCv.put("nome", episodio.nomeEp)
        episodioCv.put("duracao", episodio.duracaoEp)
        episodioCv.put("assistido", episodio.assistidoEp)
        episodioCv.put("temporada_id", episodio.temporadaId)

        return bdSeries.update("EPISODIO", episodioCv, "numero_sequencial = ? AND temporada_id = ?",
            arrayOf(episodio.numeroSequencialEp.toString(), episodio.temporadaId.toString()))
    }

    override fun removerEpisodio(temporadaId: Int, numeroSequencial: Int): Int {
        return bdSeries.delete("EPISODIO", "temporada_id = ? AND numero_sequencial = ?",
            arrayOf(temporadaId.toString(), numeroSequencial.toString()))
    }

    private fun intToBoolean(int: Int): Boolean {
        return int != 0
    }
}
