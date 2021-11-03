package com.example.seriesmanager.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seriesmanager.OnEpisodioClickListener
import com.example.seriesmanager.R
import com.example.seriesmanager.adapter.EpisodiosRvAdapter
import com.example.seriesmanager.controller.EpisodioController
import com.example.seriesmanager.databinding.ActivityEpisodioListaBinding
import com.example.seriesmanager.model.Episodio
import com.google.android.material.snackbar.Snackbar

class EpisodioListaActivity : AppCompatActivity(), OnEpisodioClickListener {
 companion object Extras {
      const val EXTRA_EPISODIO = "EXTRA_EPISODIO"
      const val EXTRA_POSICAO = "EXTRA_POSICAO"
  }

  private val activityEpisodioListaActivityBinding: ActivityEpisodioListaBinding by lazy {
      ActivityEpisodioListaBinding.inflate(layoutInflater)
  }

  private lateinit var episodioActivityResultLauncher: ActivityResultLauncher<Intent>
  private lateinit var editarEpisodioActivityResultLauncher: ActivityResultLauncher<Intent>

  //Data Source
  private val episodiosList: MutableList<Episodio> by lazy {
      episodioController.buscarEpisodio()
  }

  //Controller
  private val episodioController: EpisodioController by lazy {
      EpisodioController(this)
  }

  //Layout Manager
  private val EpisodioLayoutManager: LinearLayoutManager by lazy {
      LinearLayoutManager(this)
  }

  private val episodioAdapter: EpisodiosRvAdapter by lazy {
      EpisodiosRvAdapter(this, episodiosList)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setContentView(activityEpisodioListaActivityBinding.root)

      //Associar Adapter e Layout Manager ao Recycler View
      activityEpisodioListaActivityBinding.episodiosRv.adapter = episodioAdapter
      activityEpisodioListaActivityBinding.episodiosRv.layoutManager = EpisodioLayoutManager

      //Adicionar uma série
      episodioActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->
          if(resultado.resultCode== RESULT_OK){
              resultado.data?.getParcelableExtra<Episodio>(EXTRA_EPISODIO)?.apply {
                  episodioController.inserirEpisodio(this)
                  episodiosList.add(this)
                  episodioAdapter.notifyDataSetChanged()
              }
          }
      }

      //Editar um episódio
      editarEpisodioActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->
          if(resultado.resultCode == RESULT_OK) {
              val posicao = resultado.data?.getIntExtra(EXTRA_POSICAO, -1)
              resultado.data?.getParcelableExtra<Episodio>(EXTRA_EPISODIO)?.apply {
                  if(posicao!=null && posicao!=-1){
                      episodioController.modificarEpisodio(this)
                      episodiosList[posicao] = this
                      episodioAdapter.notifyDataSetChanged()
                  }
              }
          }
      }

      activityEpisodioListaActivityBinding.adicionarSerieFab.setOnClickListener {
          episodioActivityResultLauncher.launch(Intent(this, EpisodiosActivity::class.java))
      }
  }

  override fun onContextItemSelected(item: MenuItem): Boolean {
      val posicao = episodioAdapter.posicao
      val episodio = episodiosList[posicao]

      return when (item.itemId) {
          R.id.editarSerieMi -> {
              //Editar Temporada
              val editarEpisodioIntent = Intent(this, EpisodiosActivity::class.java)
              editarEpisodioIntent.putExtra(EXTRA_EPISODIO, episodio)
              editarEpisodioIntent.putExtra(EXTRA_POSICAO, posicao)
              editarEpisodioActivityResultLauncher.launch(editarEpisodioIntent)
              true
          }

          R.id.removerSerieMi -> {
              //Remover Temporada
              with(AlertDialog.Builder(this)) {
                  setMessage("Confirma a remoção?")
                  setPositiveButton("Sim") { _, _ ->
                      episodioController.apagarEpisodio(episodio.numeroSequencialEp)
                      episodiosList.removeAt(posicao)
                      episodioAdapter.notifyDataSetChanged()
                      Snackbar.make(
                          activityEpisodioListaActivityBinding.root,
                          "Episódio removida!",
                          Snackbar.LENGTH_SHORT
                      ).show()
                  }
                  setNegativeButton("Não") { _, _, ->
                      Snackbar.make(
                          activityEpisodioListaActivityBinding.root,
                          "Remoção cancelada!",
                          Snackbar.LENGTH_SHORT
                      ).show()
                  }
                  create()
              }.show()

              true
          }

        /*  R.id.assistidoEpMi -> {
              //Episódio Assistido
              if(episodio.assistidoEp) {
                  Toast.makeText(this,"Episódio Visto", Toast.LENGH_SHORT).show()
                  return false
              }
              episodio.assistidoEp = true
              episodioController.modificarEpisodio(episodio)
              atualizaAdapter()
          }*/
          else -> {
              false
          }

      }
  }

  fun atualizaAdapter() {
      episodioAdapter.notifyDataSetChanged()
  }

  override fun onEpisodioClick(posicao: Int) {
      val episodio = episodiosList[posicao]
      val detalhesEpisodiosIntent = Intent(this, EpisodiosActivity::class.java)
      detalhesEpisodiosIntent.putExtra(EpisodioListaActivity.EXTRA_EPISODIO, episodio)
      startActivity(detalhesEpisodiosIntent)
  }

}