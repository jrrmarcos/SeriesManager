package com.example.seriesmanager.model

import android.content.Context
import android.database.sqlite.SQLiteDatabase

class GeneroSqlite(contexto: Context): GeneroDAO {
    private val bdSeries: SQLiteDatabase = Manager(contexto).getSeriesBD()

    override fun criarGenero(serie: Genero): Long {
        TODO("Not yet implemented")
    }

    override fun recuperarGeneros(): MutableList<String> {
        val generosList: MutableList<String> = ArrayList()
        val generoCursor = bdSeries.rawQuery("SELECT DISTINCT nome_genero FROM GENERO", null)

        if(generoCursor.moveToFirst()) {
            while (!generoCursor.isAfterLast) {
                val genero: String = generoCursor.getString(generoCursor.getColumnIndexOrThrow("nome_genero"))
                generosList.add(genero)
                generoCursor.moveToNext()
            }
        }
        return generosList
    }
}