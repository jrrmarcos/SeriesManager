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
import com.example.seriesmanager.adapter.EpisodiosRvAdapter
import com.example.seriesmanager.controller.EpisodioController
import com.example.seriesmanager.databinding.ActivityEpisodioListaBinding
import com.example.seriesmanager.model.Episodio
import com.example.seriesmanager.model.Serie
import com.example.seriesmanager.model.Temporada
import com.example.seriesmanager.view.SerieListaActivity.Extras.EXTRA_SERIE
import com.example.seriesmanager.view.TemporadaListaActivity.Extras.EXTRA_ID_TEMPORADA
import com.example.seriesmanager.view.TemporadaListaActivity.Extras.EXTRA_TEMPORADA
import com.google.android.material.snackbar.Snackbar

class EpisodioListaActivity : AppCompatActivity(), OnEpisodioClickListener {
    companion object Extras {
        const val EXTRA_EPISODIO = "EXTRA_EPISODIO"
        const val EXTRA_POSICAO_EP = "EXTRA_POSICAO_EP"
    }

    private lateinit var temporada: Temporada
    private lateinit var serie: Serie

    private val activityEpisodioListaActivityBinding: ActivityEpisodioListaBinding by lazy {
        ActivityEpisodioListaBinding.inflate(layoutInflater)
    }

    private lateinit var episodioActivityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var editarEpisodioActivityResultLauncher: ActivityResultLauncher<Intent>


    //Controller
    private val episodioController: EpisodioController by lazy {
        EpisodioController(temporada)
    }

    //Data source
    private val episodioList: MutableList<Episodio> by lazy {
        episodioController.buscarEpisodios()
    }

    //Adapter
    private val episodioAdapter: EpisodiosRvAdapter by lazy {
        EpisodiosRvAdapter(this, episodioList)
    }

    //Layout Manager
    private val episodioLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityEpisodioListaActivityBinding.root)
        supportActionBar?.subtitle = "Episodios"

        temporada = intent.getParcelableExtra<Temporada>(EXTRA_TEMPORADA)!!
        serie = intent.getParcelableExtra<Serie>(EXTRA_SERIE)!!

        //Associar Adapter e Layout Manager ao Recycler View
        activityEpisodioListaActivityBinding.EpisodiosRv.adapter = episodioAdapter
        activityEpisodioListaActivityBinding.EpisodiosRv.layoutManager = episodioLayoutManager

        //Adicionar um episódio
        episodioActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->
            if (resultado.resultCode == RESULT_OK) {
                resultado.data?.getParcelableExtra<Episodio>(EXTRA_EPISODIO)?.apply {
                    episodioController.inserirEpisodio(this)
                    episodioList.add(this)
                    episodioAdapter.notifyDataSetChanged()
                }
            }
        }

        //Editar um episódio
        editarEpisodioActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->
            if (resultado.resultCode == RESULT_OK) {
                val posicao = resultado.data?.getIntExtra(EXTRA_POSICAO_EP, -1)
                resultado.data?.getParcelableExtra<Episodio>(EXTRA_EPISODIO)?.apply {
                    if (posicao != null && posicao != -1) {
                        episodioController.modificarEpisodio(this)
                        episodioList[posicao] = this
                        episodioAdapter.notifyDataSetChanged()
                    }
                }
            }
        }

        activityEpisodioListaActivityBinding.adicionarEpisodioFb.setOnClickListener {
            val addEpisodioIntent = Intent(this, EpisodioActivity::class.java)
            addEpisodioIntent.putExtra(EXTRA_ID_TEMPORADA, temporada.numeroSequencialTemp)
            episodioActivityResultLauncher.launch(addEpisodioIntent)
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val posicao = episodioAdapter.posicao
        val episodio = episodioList[posicao]

        return when(item.itemId) {
            R.id.EditarEpisodioMi -> {
                //Editar episódio
                val editarEpisodioIntent = Intent(this, EpisodioActivity::class.java)
                editarEpisodioIntent.putExtra(EXTRA_EPISODIO, episodio)
                editarEpisodioIntent.putExtra(EXTRA_POSICAO_EP, posicao)
                editarEpisodioActivityResultLauncher.launch(editarEpisodioIntent)
                true
            }
            R.id.removerEpisodioMi -> {
                //Remover episódio
                with(AlertDialog.Builder(this)) {
                    setMessage("Confirma a remoção?")
                    setPositiveButton("Sim") { _, _ ->
                        episodioController.apagarEpisodio(episodio.nomeEp, episodio.numeroSequencialEp)
                        episodioList.removeAt(posicao)
                        episodioAdapter.notifyDataSetChanged()
                        Snackbar.make(activityEpisodioListaActivityBinding.root, "Episódio removido", Snackbar.LENGTH_SHORT).show()
                    }
                    setNegativeButton("Não") { _, _ ->
                        Snackbar.make(activityEpisodioListaActivityBinding.root, "Remoção cancelada", Snackbar.LENGTH_SHORT).show()
                    }
                    create()
                }.show()
                true
            } else -> { false }
        }
    }

    override fun onEpisodioClick(posicao: Int) {
        /*temporadaId = intent.getIntExtra(EXTRA_ID_TEMPORADA, -1)
        val episodio = episodioList[posicao]
        val consultarEpisodioIntent = Intent(this, EpisodioActivity::class.java)
        consultarEpisodioIntent.putExtra(EXTRA_EPISODIO, episodio)
        consultarEpisodioIntent.putExtra(EXTRA_ID_TEMPORADA, temporadaId)
        startActivity(consultarEpisodioIntent)*/
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
        if(AutenticacaoFirebase.firebaseAuth.currentUser == null) {
            finish()
        }
    }
}