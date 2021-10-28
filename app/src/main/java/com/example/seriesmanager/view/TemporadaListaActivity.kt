package com.example.seriesmanager.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seriesmanager.OnTemporadaClickListener
import com.example.seriesmanager.R
import com.example.seriesmanager.adapter.TemporadasRvAdapter
import com.example.seriesmanager.controller.TemporadaController
import com.example.seriesmanager.databinding.ActivityTemporadaListaBinding
import com.example.seriesmanager.model.Temporada
import com.google.android.material.snackbar.Snackbar

class TemporadaListaActivity : AppCompatActivity(), OnTemporadaClickListener {
    companion object Extras {
        const val EXTRA_TEMPORADA = "EXTRA_TEMPORADA"
        const val EXTRA_POSICAO = "EXTRA_POSICAO"
    }

    private val activityTemporadaListaActivityBinding: ActivityTemporadaListaBinding by lazy {
        ActivityTemporadaListaBinding.inflate(layoutInflater)
    }

    private lateinit var temporadaActivityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var editarTemporadaActivityResultLauncher: ActivityResultLauncher<Intent>

    //Data Source
    private val temporadasList: MutableList<Temporada> by lazy {
        temporadaController.buscarTemporadas()
    }

    //Controller
    private val temporadaController: TemporadaController by lazy {
        TemporadaController(this)
    }

    //Layout Manager
    private val temporadaLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(this)
    }

    private val temporadaAdapter: TemporadasRvAdapter by lazy {
        TemporadasRvAdapter(this, temporadasList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityTemporadaListaActivityBinding.root)

        //Associar Adapter e Layout Manager ao Recycler View
        activityTemporadaListaActivityBinding.temporadasRv.adapter = temporadaAdapter
        activityTemporadaListaActivityBinding.temporadasRv.layoutManager = temporadaLayoutManager

        //Adicionar uma série
        temporadaActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->
            if(resultado.resultCode== RESULT_OK){
                resultado.data?.getParcelableExtra<Temporada>(EXTRA_TEMPORADA)?.apply {
                    temporadaController.inserirTemporada(this)
                    temporadasList.add(this)
                    temporadaAdapter.notifyDataSetChanged()
                }
            }
        }

        //Editar uma temporada
        editarTemporadaActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->
            if(resultado.resultCode == RESULT_OK) {
                val posicao = resultado.data?.getIntExtra(EXTRA_POSICAO, -1)
                resultado.data?.getParcelableExtra<Temporada>(EXTRA_TEMPORADA)?.apply {
                    if(posicao!=null && posicao!=-1){
                        temporadaController.modificarTemporada(this)
                        temporadasList[posicao] = this
                        temporadaAdapter.notifyDataSetChanged()
                    }
                }
            }
        }

        activityTemporadaListaActivityBinding.adicionarSerieFab.setOnClickListener {
            temporadaActivityResultLauncher.launch(Intent(this, TemporadasActivity::class.java))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean  = when (item.itemId) {
        R.id.adicionarSerieMi -> {
            temporadaActivityResultLauncher.launch(Intent(this, TemporadasActivity::class.java))
            true
        } else ->  {
            false;
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val posicao = temporadaAdapter.posicao
        val temporada = temporadasList[posicao]

        return when (item.itemId) {
            R.id.editarSerieMi -> {
                //Editar Temporada
                val editarTemporadaIntent = Intent(this, TemporadasActivity::class.java)
                editarTemporadaIntent.putExtra(EXTRA_TEMPORADA, temporada)
                editarTemporadaIntent.putExtra(EXTRA_POSICAO, posicao)
                editarTemporadaActivityResultLauncher.launch(editarTemporadaIntent)
                true
            }
            R.id.removerSerieMi -> {
                //Remover Temporada
                with(AlertDialog.Builder(this)) {
                    setMessage("Confirma a remoção?")
                    setPositiveButton("Sim") { _, _ ->
                        temporadaController.apagarTemporada(temporada.numeroSequencialTemp)
                        temporadasList.removeAt(posicao)
                        temporadaAdapter.notifyDataSetChanged()
                        Snackbar.make(
                            activityTemporadaListaActivityBinding.root,
                            "Temporada removida!",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    setNegativeButton("Não") { _, _, ->
                        Snackbar.make(
                            activityTemporadaListaActivityBinding.root,
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

    override fun onTemporadaClick(posicao: Int) {
        val temporada = temporadasList[posicao]
        //val abrirEpisodiosIntent = Intent(this, EpisodioListaActivity::class.java)
        //abrirEpisodiosIntent.putExtra(EXTRA_TEMPORADA, temporada)
        //startActivity(abrirEpisodiosIntent)
    }
}