package com.example.seriesmanager.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seriesmanager.OnSerieClickListener
import com.example.seriesmanager.R
import com.example.seriesmanager.adapter.SeriesRvAdapter
import com.example.seriesmanager.controller.SerieController
import com.example.seriesmanager.databinding.ActivitySerieListaBinding
import com.example.seriesmanager.model.Serie
import com.example.seriesmanager.model.Temporada
import com.google.android.material.snackbar.Snackbar

class SerieListaActivity : AppCompatActivity(), OnSerieClickListener {
    companion object Extras {
        const val EXTRA_SERIE = "EXTRA_SERIE"
        const val EXTRA_SERIE_POSICAO = "EXTRA_POSICAO_SERIE"
    }

    private val activityMainBinding: ActivitySerieListaBinding by lazy {
        ActivitySerieListaBinding.inflate(layoutInflater)
    }

    private lateinit var serieActivityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var visualizarSerieActivityResultLauncher: ActivityResultLauncher<Intent>

    //Controller
    private val serieController: SerieController by lazy {
        SerieController(this)
    }

    //Data source
    private val serieList: MutableList<Serie> by lazy {
        serieController.buscarSeries()
    }

    //Adapter
    private val serieAdapter: SeriesRvAdapter by lazy {
        SeriesRvAdapter(this, serieList)
    }

    //Layout Manager
    private val serieLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)

        activityMainBinding.SeriesRv.adapter = serieAdapter
        activityMainBinding.SeriesRv.layoutManager = serieLayoutManager

        //Associar Adapter e Layout Manager ao Recycler View
        serieActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->
            if (resultado.resultCode == RESULT_OK) {
                resultado.data?.getParcelableExtra<Serie>(EXTRA_SERIE)?.apply {
                    serieController.inserirSerie(this)
                    serieList.add(this)
                    serieAdapter.notifyDataSetChanged()
                }
            }
        }

        visualizarSerieActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->
            if (resultado.resultCode == RESULT_OK) {
                val posicao = resultado.data?.getIntExtra(EXTRA_SERIE_POSICAO, -1)
                resultado.data?.getParcelableExtra<Serie>(EXTRA_SERIE)?.apply {
                }
            }
        }

        activityMainBinding.adicionarSeriesFb.setOnClickListener {
            serieActivityResultLauncher.launch(Intent(this, SerieActivity::class.java))
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val posicao = serieAdapter.posicao
        val serie = serieList[posicao]

        return when(item.itemId) {
            R.id.visualizarSerieMi -> {
                //Visualizar uma série
                val visualizarSerieIntent = Intent(this, SerieActivity::class.java)
                visualizarSerieIntent.putExtra(EXTRA_SERIE, serie)
                visualizarSerieIntent.putExtra(EXTRA_SERIE_POSICAO, posicao)
                visualizarSerieActivityResultLauncher.launch(visualizarSerieIntent)
                true
            }
            R.id.removerSerieMi -> {
                //Remover uma série
                with(AlertDialog.Builder(this)) {
                    setMessage("Confirma a remoção?")
                    setPositiveButton("Sim") { _, _ ->
                        serieController.apagarSerie(serie.nomeSerie)
                        serieList.removeAt(posicao)
                        serieAdapter.notifyDataSetChanged()
                        Snackbar.make(activityMainBinding.root, "Serie removida", Snackbar.LENGTH_SHORT).show()
                    }
                    setNegativeButton("Não") { _, _ ->
                        Snackbar.make(activityMainBinding.root, "Remoção cancelada", Snackbar.LENGTH_SHORT).show()
                    }
                    create()
                }.show()

                true
            } else -> { false }
        }
    }

    override fun onSerieClick(posicao: Int) {
        val serie = serieList[posicao]
        val consultarTemporadasIntent = Intent(this, TemporadaListaActivity::class.java)
        consultarTemporadasIntent.putExtra(EXTRA_SERIE, serie)
        startActivity(consultarTemporadasIntent)
    }
}