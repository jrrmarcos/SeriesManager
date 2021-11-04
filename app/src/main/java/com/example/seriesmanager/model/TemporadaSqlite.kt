package com.example.seriesmanager.model

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase

class TemporadaSqlite (contexto: Context): TemporadaDAO {
    private val bdSeries: SQLiteDatabase = Manager(contexto).getSeriesBD()

    override fun criarTemporada(temporada: Temporada): Long {
        val temporadaCv = ContentValues()
        temporadaCv.put("numero_sequencial", temporada.numeroSequencialTemp)
        temporadaCv.put("ano_lancamento", temporada.anoLancamentoTemp)
        temporadaCv.put("nome_serie", temporada.nomeSerie)

        return bdSeries.insert("TEMPORADA", null, temporadaCv)
    }

    override fun recuperarTemporadas(nomeSerie: String): MutableList<Temporada> {
        val temporadasList: MutableList<Temporada> = ArrayList()
        val temporadaCursor = bdSeries.rawQuery("SELECT nome_serie, ano_lancamento, numero_sequencial " +
                "                                    FROM temporada WHERE nome_serie = ?;", arrayOf(nomeSerie))
        val temporada: Temporada

        if (temporadaCursor.moveToFirst()) {
            while (!temporadaCursor.isAfterLast) {
                val temporada = Temporada(
                    temporadaCursor.getInt(temporadaCursor.getColumnIndexOrThrow("numero_sequencial")),
                    temporadaCursor.getString(temporadaCursor.getColumnIndexOrThrow("ano_lancamento")),
                    temporadaCursor.getString(temporadaCursor.getColumnIndexOrThrow("qtd_episodios")),
                    temporadaCursor.getString(temporadaCursor.getColumnIndexOrThrow("nome_serie"))
                )
                temporadasList.add(temporada)
                temporadaCursor.moveToNext()
            }
        }
        return temporadasList
    }

    override fun removerTemporada(nomeSerie: String, numeroSequencial: Int): Int {
        val numeroSequencialtoString: String = numeroSequencial.toString()
        val temporadaId = buscarTemporadaId(nomeSerie, numeroSequencial)

        //É necessário deletar todos os episódios antes de deletar a temporada
        bdSeries.delete("EPISODIO", "temporada_id = ? ", arrayOf(temporadaId.toString()))

        //Episódios deletados, deletando temporada
        return bdSeries.delete("TEMPORADA", "numero_sequencial_temp = ? AND nome_serie = ?",
            arrayOf(numeroSequencialtoString, nomeSerie)
        )
    }

    override fun buscarTemporadaId(nomeSerie: String, numeroSequencial: Int): Int {
        val temporadaCursor = bdSeries.rawQuery("SELECT id " +
                "                                   from temporada WHERE numero_sequencial_temp = ? AND nome_serie = ?",
            arrayOf(numeroSequencial.toString(), nomeSerie))
        var temporadaId: Int = 0

        if (temporadaCursor.moveToFirst()){
            temporadaId = temporadaCursor.getInt(temporadaCursor.getColumnIndexOrThrow("id_temp"))
        }
        return temporadaId
    }
}