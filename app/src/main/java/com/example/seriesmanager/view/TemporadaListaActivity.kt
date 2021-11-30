package com.example.seriesmanager.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seriesmanager.R
import com.example.seriesmanager.adapter.TemporadasRvAdapter
import com.example.seriesmanager.controller.TemporadaController
import com.example.seriesmanager.databinding.ActivityTemporadaListaBinding
import com.example.seriesmanager.model.Serie
import com.example.seriesmanager.model.Temporada
import com.example.seriesmanager.view.SerieListaActivity.Extras.EXTRA_SERIE
import com.google.android.material.snackbar.Snackbar

class TemporadaListaActivity : AppCompatActivity(), OnTemporadaClickListener {
    companion object Extras {
        const val EXTRA_TEMPORADA = "EXTRA_TEMPORADA"
        const val EXTRA_POSICAO_TEMP = "EXTRA_POSICAO_TEMP"
        const val EXTRA_ID_TEMPORADA = "EXTRA_ID_TEMPORADA"
    }

    private lateinit var serie: Serie

    private val activityTemporadaListaActivityBinding: ActivityTemporadaListaBinding by lazy {
        ActivityTemporadaListaBinding.inflate(layoutInflater)
    }

    private lateinit var temporadaActivityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var visualizarTemporadaActivityResultLauncher: ActivityResultLauncher<Intent>

    // Controller
    private val temporadaController: TemporadaController by lazy {
        TemporadaController(serie)
    }

    //Data source
    private val temporadaList: MutableList<Temporada> by lazy {
        temporadaController.buscarTemporadas(serie.nomeSerie)
    }

    //Adapter
    private val temporadaAdapter: TemporadasRvAdapter by lazy {
        TemporadasRvAdapter(this, temporadaList)
    }

    //Layout Manager
    private val temporadaLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityTemporadaListaActivityBinding.root)

        serie = intent.getParcelableExtra<Serie>(EXTRA_SERIE)!!

        //Associar Adapter e Layout Manager ao Recycler View
        activityTemporadaListaActivityBinding.TemporadasRv.adapter = temporadaAdapter
        activityTemporadaListaActivityBinding.TemporadasRv.layoutManager = temporadaLayoutManager

        //Adicionar uma temporada
        temporadaActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->
            if (resultado.resultCode == RESULT_OK) {
                resultado.data?.getParcelableExtra<Temporada>(EXTRA_TEMPORADA)?.apply {
                    temporadaController.inserirTemporada(this)
                    temporadaList.add(this)
                    temporadaAdapter.notifyDataSetChanged()
                }
            }
        }

        visualizarTemporadaActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->
            if (resultado.resultCode == RESULT_OK) {
                val posicao = resultado.data?.getIntExtra(EXTRA_POSICAO_TEMP, -1)
                resultado.data?.getParcelableExtra<Temporada>(EXTRA_TEMPORADA)?.apply {
                }
            }
        }

        activityTemporadaListaActivityBinding.adicionarTemporadaFb.setOnClickListener {
            val addTemporadaIntent = Intent(this, TemporadaActivity::class.java)
            addTemporadaIntent.putExtra(EXTRA_SERIE, serie)
            temporadaActivityResultLauncher.launch(addTemporadaIntent)
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val posicao = temporadaAdapter.posicao
        val temporada = temporadaList[posicao]

        return when(item.itemId) {
            R.id.visualizarTemporadaMi -> {
                //Editar temporada
                val visualizarTemporadaIntent = Intent(this, TemporadaActivity::class.java)
                visualizarTemporadaIntent.putExtra(EXTRA_TEMPORADA, temporada)
                visualizarTemporadaIntent.putExtra(EXTRA_POSICAO_TEMP, posicao)
                visualizarTemporadaIntent.putExtra(EXTRA_SERIE, serie)
                visualizarTemporadaActivityResultLauncher.launch(visualizarTemporadaIntent)
                true
            }
            R.id.removerTemporadaMi -> {
                //Remover temporada
                with(AlertDialog.Builder(this)) {
                    setMessage("Confirma a remoção?")
                    setPositiveButton("Sim") { _, _ ->
                        temporadaController.apagarTemporadas(serie.nomeSerie, temporada.numeroSequencialTemp)
                        temporadaList.removeAt(posicao)
                        temporadaAdapter.notifyDataSetChanged()
                        Snackbar.make(activityTemporadaListaActivityBinding.root, "Temporada removida", Snackbar.LENGTH_SHORT).show()
                    }
                    setNegativeButton("Não") { _, _ ->
                        Snackbar.make(activityTemporadaListaActivityBinding.root, "Remoção cancelada", Snackbar.LENGTH_SHORT).show()
                    }
                    create()
                }.show()

                true
            } else -> { false }
        }
    }

    override fun onTemporadaClick(posicao: Int) {
        val temporada = temporadaList[posicao]
        //val temporadaId = temporadaController.buscarTemporadaId(temporada.nomeSerie, temporada.numeroSequencialTemp)
        val consultarEpisodiosIntent = Intent(this, EpisodioListaActivity::class.java)
        consultarEpisodiosIntent.putExtra(EXTRA_TEMPORADA, temporada)
        consultarEpisodiosIntent.putExtra(EXTRA_SERIE, serie)
        //consultarEpisodiosIntent.putExtra(EXTRA_ID_TEMPORADA, temporadaId)
        startActivity(consultarEpisodiosIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean  = when (item.itemId) {
        R.id.sairMi -> {
            AutenticacaoFirebase.firebaseAuth.signOut()
            finish()
            true
        }else ->  {
            false;
        }
    }

    override fun onStart() {
        super.onStart()
        if (AutenticacaoFirebase.firebaseAuth.currentUser == null) {
            finish()
        }
    }
}