package com.example.seriesmanager.model

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase

class SerieSqlite  (contexto: Context): SerieDAO {
    private val bdSeries: SQLiteDatabase = Manager(contexto).getSeriesBD()

    override fun criarSerie(serie: Serie): Long {
        val serieCv = ContentValues()
        serieCv.put("nome_serie", serie.nomeSerie)
        serieCv.put("ano_lancamento", serie.anoLancamentoSerie)
        serieCv.put("emissora", serie.emissoraSerie)
        serieCv.put("genero", serie.generoSerie)

        return bdSeries.insert("SERIE", null, serieCv)
    }

    override fun recuperarSeries(): MutableList<Serie> {
        val series: MutableList<Serie> = ArrayList()
        val serieCursor = bdSeries.rawQuery("SELECT * FROM SERIE", null)

        if (serieCursor.moveToFirst()) {
            while (!serieCursor.isAfterLast) {
                val serie: Serie = Serie(
                    serieCursor.getString(serieCursor.getColumnIndexOrThrow("nome_serie")),
                    serieCursor.getString(serieCursor.getColumnIndexOrThrow("ano_lancamento")),
                    serieCursor.getString(serieCursor.getColumnIndexOrThrow("emissora")),
                    serieCursor.getString(serieCursor.getColumnIndexOrThrow("genero")),
                )
                series.add(serie)
                serieCursor.moveToNext()
            }
        }
        return series
    }

    override fun removerSerie(nome: String): Int {

        return bdSeries.delete("SERIE", "nome_serie = ?", arrayOf(nome))
    }
}