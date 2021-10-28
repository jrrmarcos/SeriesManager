package com.example.seriesmanager.model

import android.content.ContentValues
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import java.sql.SQLException

class SerieSqlite(contexto: Context): SerieDAO {

    companion object {
        private val BD_SERIES = "series"
        private val TABELA_SERIE = "serie"

        //<><><><><>><><><><><><> ADIÇÃO DE COLUNAS REF À SÉRIE
        private val COLUNA_NOME = "nome"
        private val COLUNA_ANO_LANCAMENTO = "ano_lancamento"
        private val COLUNA_EMISSORA = "emissora"
        private val COLUNA_GENERO = "genero"

        //<><><><><><><><><><><> ADIÇÃO DE COLUNAS REF À TEMPORADAS
        private val TABELA_TEMPORADA = "temporada"
        private val COLUNA_NRO_SEQUENCIAL_TEMP = "numero_temporada"
        private val COLUNA_ANO_LANCAMENTO_TEMP = "ano_lancamento_temp"
        private val COLUNA_QTD_EPISODIOS = "qtd_episodios"

        //<><><><><><><>><><><> ADIÇÃO DE COLUNAS REF À EPISÓDIOS
        private val TABELA_EPISODIO = "episodio"
        private val COLUNA_NRO_SEQUENCIAL_EP = "numero_episodio"
        private val COLUNA_NOME_EP = "nome_episodio"
        private val COLUNA_TEMPO_EP = "tempo_episodio"
        private val COLUNA_ASSISTIDO = "episodio_assistido"


        //CRIANDO TABELA DE SÉRIES
        private val CRIAR_TABELA_SERIES_STMT = "CREATE TABLE IF NOT EXISTS ${TABELA_SERIE} (" +
                "${COLUNA_NOME} TEXT NOT NULL PRIMARY KEY, " +
                "${COLUNA_ANO_LANCAMENTO} TEXT NOT NULL, " +
                "${COLUNA_EMISSORA} TEXT NOT NULL, " +
                "${COLUNA_GENERO} TEXT NOT NULL);"

        //CRIANDO TABELA DE TEMPORADAS
        private val CRIAR_TABELA_TEMPORADAS_STMT = "CREATE TABLE IF NOT EXISTS ${TABELA_TEMPORADA} (" +
                "${COLUNA_NRO_SEQUENCIAL_TEMP} INT NOT NULL, " +
                "${COLUNA_ANO_LANCAMENTO_TEMP} TEXT NOT NULL, " +
                "${COLUNA_QTD_EPISODIOS} INT NOT NULL," +
                "${COLUNA_NOME} TEXT NOT NULL, " +
                "FOREIGN KEY (${COLUNA_NOME}) REFERENCES ${TABELA_SERIE} (${COLUNA_NOME}));"

        //CRIANDO TABELA DE EPISÓDIOS
        private val CRIAR_TABELA_EPISODIOS_STMT = "CREATE TABLE IF NOT EXISTS ${TABELA_EPISODIO} (" +
                "${COLUNA_NRO_SEQUENCIAL_EP} INT NOT NULL, " +
                "${COLUNA_NOME_EP} TEXT NOT NULL, " +
                "${COLUNA_TEMPO_EP} TEXT NOT NULL, " +
                "${COLUNA_ASSISTIDO} BOOLEAN NOT NULL);"
    }

    //Referência para o banco de dados
    private val seriesBd: SQLiteDatabase
    init {
        seriesBd = contexto.openOrCreateDatabase(BD_SERIES, MODE_PRIVATE, null)
        try{
            seriesBd.execSQL(CRIAR_TABELA_SERIES_STMT)
        } catch (se: SQLException) {
            Log.e("Series", se.toString())
        }
    }

    // GERENCIAMENTO DE TEMPORADAS
    override fun criarSerie(serie: Serie): Long {
        val serieCv = ContentValues()
        serieCv.put(COLUNA_NOME, serie.nome)
        serieCv.put(COLUNA_ANO_LANCAMENTO, serie.ano_lancamento)
        serieCv.put(COLUNA_EMISSORA, serie.emissora)
        serieCv.put(COLUNA_GENERO, serie.genero)

        return seriesBd.insert(TABELA_SERIE, null, serieCv)
    }

    override fun recuperarSerie(nome: String): Serie {
        val serieCursor = seriesBd.query(
            true,
            TABELA_SERIE,
            null,
            "${COLUNA_NOME} = ?",
            arrayOf(nome),
            null,
            null,
            null,
            null
        )

        return if (serieCursor.moveToFirst()) {
            with(serieCursor) {
                Serie(
                    getString(getColumnIndexOrThrow(TABELA_SERIE)),
                    getString(getColumnIndexOrThrow(COLUNA_ANO_LANCAMENTO)),
                    getString(getColumnIndexOrThrow(COLUNA_EMISSORA)),
                    getString(getColumnIndexOrThrow(COLUNA_GENERO)),
                )
            }
        } else {
            Serie()
        }
    }

    override fun recuperarSeries(): MutableList<Serie> {
        val listaSeries: MutableList<Serie> = mutableListOf()

        val serieQuery = "SELECT * FROM ${TABELA_SERIE};"
        val serieCursor = seriesBd.rawQuery(serieQuery,null)

        while(serieCursor.moveToNext()) {
            with(serieCursor){
            listaSeries.add (Serie(
                getString(getColumnIndexOrThrow(COLUNA_NOME)),
                getString(getColumnIndexOrThrow(COLUNA_ANO_LANCAMENTO)),
                getString(getColumnIndexOrThrow(COLUNA_EMISSORA)),
                getString(getColumnIndexOrThrow(COLUNA_GENERO))
            ))
            }
        }
        return listaSeries
    }

    override fun atualizarSerie(serie: Serie): Int {
        val serieCv = ContentValues()
        serieCv.put(COLUNA_NOME, serie.nome)
        serieCv.put(COLUNA_ANO_LANCAMENTO, serie.ano_lancamento)
        serieCv.put(COLUNA_EMISSORA, serie.emissora)
        serieCv.put(COLUNA_GENERO, serie.genero)

        return seriesBd.update(TABELA_SERIE, serieCv, "${COLUNA_NOME} = ?", arrayOf(serie.nome))
    }

    override fun removerSerie(nome: String): Int {
        return seriesBd.delete (
            TABELA_SERIE,
            "${COLUNA_NOME} = ?",
            arrayOf(nome)
        )
    }

    /*
    // GERENCIAMENTO DE TEMPORADAS
    override fun criarTemporada(serie: Serie): Long {
        val serieCv = ContentValues()
        serieCv.put(COLUNA_NOME, serie.nome)
        serieCv.put(COLUNA_ANO_LANCAMENTO, serie.ano_lancamento)
        serieCv.put(COLUNA_EMISSORA, serie.emissora)
        serieCv.put(COLUNA_GENERO, serie.genero)

        return seriesBd.insert(TABELA_SERIE, null, serieCv)
    }

    override fun recuperarSerie(nome: String): Serie {
        val serieCursor = seriesBd.query(
            true,
            TABELA_SERIE,
            null,
            "${COLUNA_NOME} = ?",
            arrayOf(nome),
            null,
            null,
            null,
            null
        )

        return if (serieCursor.moveToFirst()) {
            with(serieCursor) {
                Serie(
                    getString(getColumnIndexOrThrow(TABELA_SERIE)),
                    getString(getColumnIndexOrThrow(COLUNA_ANO_LANCAMENTO)),
                    getString(getColumnIndexOrThrow(COLUNA_EMISSORA)),
                    getString(getColumnIndexOrThrow(COLUNA_GENERO)),
                )
            }
        } else {
            Serie()
        }
    }

    override fun recuperarSeries(): MutableList<Serie> {
        val listaSeries: MutableList<Serie> = mutableListOf()

        val serieQuery = "SELECT * FROM ${TABELA_SERIE};"
        val serieCursor = seriesBd.rawQuery(serieQuery,null)

        while(serieCursor.moveToNext()) {
            with(serieCursor){
                listaSeries.add (Serie(
                    getString(getColumnIndexOrThrow(COLUNA_NOME)),
                    getString(getColumnIndexOrThrow(COLUNA_ANO_LANCAMENTO)),
                    getString(getColumnIndexOrThrow(COLUNA_EMISSORA)),
                    getString(getColumnIndexOrThrow(COLUNA_GENERO))
                ))
            }
        }
        return listaSeries
    }

    override fun atualizarSerie(serie: Serie): Int {
        val serieCv = ContentValues()
        serieCv.put(COLUNA_NOME, serie.nome)
        serieCv.put(COLUNA_ANO_LANCAMENTO, serie.ano_lancamento)
        serieCv.put(COLUNA_EMISSORA, serie.emissora)
        serieCv.put(COLUNA_GENERO, serie.genero)

        return seriesBd.update(TABELA_SERIE, serieCv, "${COLUNA_NOME} = ?", arrayOf(serie.nome))
    }

    override fun removerSerie(nome: String): Int {
        return seriesBd.delete (
            TABELA_SERIE,
            "${COLUNA_NOME} = ?",
            arrayOf(nome)
        )
    }
    */

}