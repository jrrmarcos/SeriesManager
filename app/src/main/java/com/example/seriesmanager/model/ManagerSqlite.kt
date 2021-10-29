package com.example.seriesmanager.model

import android.content.ContentValues
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import java.sql.SQLException

class ManagerSqlite(contexto: Context): ManagerDAO {

    companion object {

        private val BD_SERIES = "series"
        private val TABELA_SERIE = "serie"

        //ADIÇÃO DE COLUNAS REF À SÉRIE
        private val COLUNA_NOME = "nome"
        private val COLUNA_ANO_LANCAMENTO = "ano_lancamento"
        private val COLUNA_EMISSORA = "emissora"
        private val COLUNA_GENERO = "genero"

        //COLUNAS REF À TEMPORADAS
        private val TABELA_TEMPORADA = "temporada"
        private val COLUNA_NRO_SEQUENCIAL_TEMP = "numero_temporada"
        private val COLUNA_ANO_LANCAMENTO_TEMP = "ano_lancamento_temp"
        private val COLUNA_QTD_EPISODIOS = "qtd_episodios"

        //COLUNAS REF À EPISÓDIOS
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
                "${COLUNA_NRO_SEQUENCIAL_TEMP} TEXT NOT NULL, " +
                "${COLUNA_ANO_LANCAMENTO_TEMP} TEXT NOT NULL, " +
                "${COLUNA_QTD_EPISODIOS} TEXT NOT NULL," +
                "${COLUNA_NOME} TEXT NOT NULL, " +
                "FOREIGN KEY (${COLUNA_NOME}) REFERENCES ${TABELA_SERIE} (${COLUNA_NOME}) ON DELETE CASCADE);"

        //CRIANDO TABELA DE EPISÓDIOS
        private val CRIAR_TABELA_EPISODIOS_STMT = "CREATE TABLE IF NOT EXISTS ${TABELA_EPISODIO} (" +
                "${COLUNA_NRO_SEQUENCIAL_EP} TEXT NOT NULL, " +
                "${COLUNA_NOME_EP} TEXT NOT NULL, " +
                "${COLUNA_TEMPO_EP} TEXT NOT NULL, " +
                "${COLUNA_ASSISTIDO} BOOLEAN NOT NULL, " +
                "${COLUNA_NOME} TEXT NOT NULL, " +
                "FOREIGN KEY (${COLUNA_NOME}) REFERENCES ${TABELA_SERIE} (${COLUNA_NOME}) ON DELETE CASCADE);"
    }

    //Referência para o banco de dados
    private val seriesBd: SQLiteDatabase
    init {
        seriesBd = contexto.openOrCreateDatabase(BD_SERIES, MODE_PRIVATE, null)
        try{
            seriesBd.execSQL(CRIAR_TABELA_SERIES_STMT)
            seriesBd.execSQL(CRIAR_TABELA_TEMPORADAS_STMT)
            seriesBd.execSQL(CRIAR_TABELA_EPISODIOS_STMT)
        } catch (se: SQLException) {
            Log.e("Series", se.toString())
        }
    }

    //<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>
    //<><><><><><><><><><> FUNÇÕES DE SÉRIES <><><><><<><><><><>
    //<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>

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

    //<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>
    //<><><><><><><><><><> FUNÇÕES DE TEMPORADA <><><><><<><><><><>
    //<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>

    override fun criarTemporada(temporada: Temporada): Long {
        val temporadaCv = ContentValues()
        temporadaCv.put(COLUNA_NRO_SEQUENCIAL_TEMP, temporada.numeroSequencialTemp)
        temporadaCv.put(COLUNA_QTD_EPISODIOS, temporada.qtdEpisodiosTemp)
        temporadaCv.put(COLUNA_ANO_LANCAMENTO, temporada.anoLancamentoTemp)

        return seriesBd.insert(TABELA_TEMPORADA, null, temporadaCv)
    }

    override fun recuperarTemporada(numero: String): Temporada {
        val temporadaCursor = seriesBd.query(
            true,
            TABELA_TEMPORADA,
            null,
            "${COLUNA_NRO_SEQUENCIAL_TEMP} = ?",
            arrayOf(numero),
            null,
            null,
            null,
            null
        )

        return if (temporadaCursor.moveToFirst()) {
            //with(temporadaCursor) {
                Temporada(
                    //temporadaCursor.getString(temporadaCursor.getColumnIndexOrThrow(TABELA_TEMPORADA)),
                    temporadaCursor.getString(temporadaCursor.getColumnIndexOrThrow(COLUNA_NRO_SEQUENCIAL_TEMP)),
                    temporadaCursor.getString(temporadaCursor.getColumnIndexOrThrow(COLUNA_QTD_EPISODIOS)),
                    temporadaCursor.getString(temporadaCursor.getColumnIndexOrThrow(COLUNA_ANO_LANCAMENTO_TEMP))
                )
            } else {
                Temporada()
            }
    }

    override fun recuperarTemporada(): MutableList<Temporada> {
        val listaTemporada: MutableList<Temporada> = mutableListOf()

        val temporadaQuery = "SELECT * FROM ${TABELA_TEMPORADA};"
        val temporadaCursor = seriesBd.rawQuery(temporadaQuery, null)

        while (temporadaCursor.moveToNext()) {
            with(temporadaCursor) {
                listaTemporada.add( Temporada(
                        getString(getColumnIndexOrThrow(COLUNA_NRO_SEQUENCIAL_TEMP)),
                        getString(getColumnIndexOrThrow(COLUNA_QTD_EPISODIOS)),
                        getString(getColumnIndexOrThrow(COLUNA_ANO_LANCAMENTO_TEMP))
                    ))
            }
        }
        return listaTemporada
    }


    override fun atualizarTemporada(temporada: Temporada): Int {
        val temporadaCv = ContentValues()
        temporadaCv.put(COLUNA_QTD_EPISODIOS, temporada.qtdEpisodiosTemp)
        temporadaCv.put(COLUNA_ANO_LANCAMENTO_TEMP, temporada.anoLancamentoTemp)

        return seriesBd.update(TABELA_TEMPORADA, temporadaCv, "${COLUNA_NRO_SEQUENCIAL_TEMP} = ?", arrayOf(temporada.numeroSequencialTemp))
    }

    override fun removerTemporada(numero: String): Int {
        return seriesBd.delete (
            TABELA_TEMPORADA,
            "${COLUNA_NRO_SEQUENCIAL_TEMP} = ?",
            arrayOf(numero)
        )
    }

    //<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>
    //<><><><><><><><><><> FUNÇÕES DE EPISÓDIO <><><><><<><><><><>
    //<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>

    override fun criarEpisodio(episodio: Episodio): Long {
        val episodioCv = ContentValues()

        episodioCv.put(COLUNA_NRO_SEQUENCIAL_EP, episodio.numeroSequencialEp)
        episodioCv.put(COLUNA_NOME_EP, episodio.nomeEp)
        episodioCv.put(COLUNA_TEMPO_EP, episodio.tempoDuracaoEp)
        //episodioCv.put(COLUNA_TEMPO_EP, episodio.assistidoEp)

        return seriesBd.insert(TABELA_EPISODIO, null, episodioCv)
    }

    override fun recuperarEpisodio(numero: String): Episodio {
        val episodioCursor = seriesBd.query(
            TABELA_EPISODIO,
            null,
            "${COLUNA_NRO_SEQUENCIAL_EP} = ?",
            arrayOf(numero),
            null,
            null,
            null
        )

        return if (episodioCursor.moveToFirst()) {
            Episodio (
                episodioCursor.getString(episodioCursor.getColumnIndexOrThrow(COLUNA_NRO_SEQUENCIAL_EP)),
                episodioCursor.getString(episodioCursor.getColumnIndexOrThrow(COLUNA_NOME_EP)),
                episodioCursor.getString(episodioCursor.getColumnIndexOrThrow(COLUNA_TEMPO_EP))
                //episodioCursor.getString(episodioCursor.getColumnIndexOrThrow(COLUNA_ASSISTIDO)),
            )
        } else {
            Episodio()
        }
    }

    override fun recuperarEpisodio(): MutableList<Episodio> {
        val listaEpisodio: MutableList<Episodio> = mutableListOf()

        val episodioQuery = "SELECT * FROM ${TABELA_EPISODIO};"
        val episodioCursor = seriesBd.rawQuery(episodioQuery, null)

        while(episodioCursor.moveToNext()) {
            with(episodioCursor) {
                listaEpisodio.add (
                    Episodio (
                        getString(getColumnIndexOrThrow(COLUNA_NRO_SEQUENCIAL_EP)),
                        getString(getColumnIndexOrThrow(COLUNA_NOME_EP)),
                        getString(getColumnIndexOrThrow(COLUNA_TEMPO_EP))
                        //episodioCursor.getString(episodioCursor.getColumnIndexOrThrow(COLUNA_ASSISTIDO))
                    )
                )
            }
        }
        return listaEpisodio
    }

    override fun atualizarEpisodio(episodio: Episodio): Int {
        val episodioCv = ContentValues()
        episodioCv.put(COLUNA_NOME_EP, episodio.nomeEp)
        episodioCv.put(COLUNA_TEMPO_EP, episodio.tempoDuracaoEp)
        //episodioCv.put(COLUNA_ASSISTIDO, episodio.assistidoEp)

        return seriesBd.update(TABELA_EPISODIO, episodioCv, "${COLUNA_NRO_SEQUENCIAL_EP} = ?", arrayOf(episodio.numeroSequencialEp))
    }

    override fun removerEpisodio(numero: String): Int {
        return seriesBd.delete (
            TABELA_EPISODIO,
            "${COLUNA_NRO_SEQUENCIAL_EP} = ?",
            arrayOf(numero)
        )
    }
}