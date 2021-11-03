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
        const val EXTRA_POSICAO = "EXTRA_POSICAO"
    }

    private val activityMainBinding: ActivitySerieListaBinding by lazy {
        ActivitySerieListaBinding.inflate(layoutInflater)
    }

    private lateinit var serieActivityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var editarSerieActivityResultLauncher: ActivityResultLauncher<Intent>

    //Data Source
    private val seriesList: MutableList<Serie> by lazy {
        serieController.buscarSeries()
    }

    //Controller
    private val serieController: SerieController by lazy {
        SerieController(this)
    }

    //Layout Manager
    private val seriesLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(this)
    }

    private val seriesAdapter: SeriesRvAdapter by lazy {
        SeriesRvAdapter(this, seriesList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)

        //Associar Adapter e Layout Manager ao Recycler View
        activityMainBinding.seriesRv.adapter = seriesAdapter
        activityMainBinding.seriesRv.layoutManager = seriesLayoutManager

        //Adicionar uma série
        serieActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {resultado ->
            if(resultado.resultCode== RESULT_OK){
                resultado.data?.getParcelableExtra<Serie>(EXTRA_SERIE)?.apply {
                    serieController.inserirSerie(this)
                    seriesList.add(this)
                    seriesAdapter.notifyDataSetChanged()
                }
            }
        }

        //Editar uma série
        editarSerieActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {resultado ->
            if(resultado.resultCode == RESULT_OK) {
                val posicao = resultado.data?.getIntExtra(EXTRA_POSICAO, -1)
                resultado.data?.getParcelableExtra<Serie>(EXTRA_SERIE)?.apply {
                    if(posicao!=null && posicao!=-1){
                        serieController.modificarSerie(this)
                        seriesList[posicao] = this
                        seriesAdapter.notifyDataSetChanged()
                    }
                }
            }
        }

        activityMainBinding.adicionarSerieFab.setOnClickListener {
            serieActivityResultLauncher.launch(Intent(this, SerieActivity::class.java))
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val posicao = seriesAdapter.posicao
        val serie = seriesList[posicao]

        return when (item.itemId) {
            R.id.editarSerieMi -> {
                //Editar série
                val editarSerieIntent = Intent(this, SerieActivity::class.java)
                editarSerieIntent.putExtra(EXTRA_SERIE, serie)
                editarSerieIntent.putExtra(EXTRA_POSICAO, posicao)
                editarSerieActivityResultLauncher.launch(editarSerieIntent)
                true
            }
            R.id.removerSerieMi -> {
                //Remover série
                with(AlertDialog.Builder(this)) {
                    setMessage("Confirma a remoção?")
                    setPositiveButton("Sim") { _, _ ->
                        serieController.apagarSerie(serie.nome)
                        seriesList.removeAt(posicao)
                        seriesAdapter.notifyDataSetChanged()
                        Snackbar.make(
                            activityMainBinding.root,
                            "Série removida!",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    setNegativeButton("Não") { _, _, ->
                        Snackbar.make(
                            activityMainBinding.root,
                            "Remoção cancelada!",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    create()
                }.show()

                true
            }
            else -> {
                false

            }
        }
    }

    override fun onSerieClick(posicao: Int) {
        val serie = seriesList[posicao]
        val abrirTemporadaIntent = Intent(this, TemporadaListaActivity::class.java)
        abrirTemporadaIntent.putExtra(EXTRA_SERIE, serie)
        startActivity(abrirTemporadaIntent)
    }
}