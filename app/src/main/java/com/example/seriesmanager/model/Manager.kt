package com.example.seriesmanager.model

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import java.sql.SQLException

class Manager (contexto: Context) {
    companion object {
        private val SERIES_BD = "seriesBD"

        //<><><><><><><><><> CRIANDO TABELAS DE SÉRIES <><><><><><><><><><><><><><><>
        private val CRIAR_TABELA_SERIE_STMT = "CREATE TABLE IF NOT EXISTS SERIE (" +
                "nome_serie TEXT NOT NULL PRIMARY KEY, " +
                "ano_lancamento TEXT NOT NULL, " +
                "emissora TEXT NOT NULL, " +
                "genero TEXT NOT NULL, " +
                "FOREIGN KEY(genero) REFERENCES GENERO(nome));"

        //<><><><><><><><><> CRIANDO TABELA DE TEMPORADAS <><><><><><><><><><><><><><>
        private val CRIAR_TABELA_TEMPORADA_STMT = "CREATE TABLE IF NOT EXISTS TEMPORADA (" +
                "id_temporada INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "numero_sequencial INTEGER NOT NULL, " +
                "ano_lancamento TEXT NOT NULL, " +
                "qtd_episodios TEXT NOT NULL, " +
                "nome_serie TEXT NOT NULL, " +
                "FOREIGN KEY(nome_serie) REFERENCES SERIE(nome));"

        //<><><><><><><><><> CRIANDO TABELA DE EPISÓDIOS <><><><><><><><><><><><><><>
        private val CRIAR_TABELA_EPISODIO_STMT = "CREATE TABLE IF NOT EXISTS EPISODIO (" +
                "id_episodio INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "numero_sequencial INTEGER NOT NULL, " +
                "nome TEXT NOT NULL, " +
                "duracao INTEGER NOT NULL, " +
                "assistido INTEGER NOT NULL DEFAULT 0, " +
                "temporada_id INTEGER NOT NULL, " +
                "FOREIGN KEY(temporada_id) REFERENCES TEMPORADA(id));"

        //<><><><><><><><><> CRIANDO TABELA DE GENEROS <><><><><><><><><><><><><><>
        private val CRIAR_TABELA_GENERO_STMT = "CREATE OR ALTER TABLE IF NOT EXISTS GENERO (" +
                "id_genero INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "nome_genero TEXT NOT NULL);"

        //INSERINDO VALORES DEFAULT CONFORME SOLICITADO NO EXERCÍCIO
        private val INSERT_ROMANCE_TABELA_GENERO_STMT = "INSERT INTO genero (nome) VALUES('Romance');"
        private val INSERT_AVENTURA_TABELA_GENERO_STMT = "INSERT INTO genero (nome) VALUES('Aventura');"
        private val INSERT_TERROR_TABELA_GENERO_STMT = "INSERT INTO genero (nome) VALUES('Terror');"
    }

    // Referencia para o banco de dados
    private val seriesBD: SQLiteDatabase = contexto.openOrCreateDatabase(SERIES_BD, Context.MODE_PRIVATE, null)

    init {
        try {
            seriesBD.execSQL(CRIAR_TABELA_GENERO_STMT)
            seriesBD.execSQL(INSERT_ROMANCE_TABELA_GENERO_STMT)
            seriesBD.execSQL(INSERT_AVENTURA_TABELA_GENERO_STMT)
            seriesBD.execSQL(INSERT_TERROR_TABELA_GENERO_STMT)
            seriesBD.execSQL(CRIAR_TABELA_SERIE_STMT)
            seriesBD.execSQL(CRIAR_TABELA_TEMPORADA_STMT)
            seriesBD.execSQL(CRIAR_TABELA_EPISODIO_STMT)
        } catch (se: SQLException) {
            Log.e(contexto.getString(R.string.app_name), se.toString())
        }
    }

    fun getSeriesBD(): SQLiteDatabase {
        return seriesBD;
    }
}