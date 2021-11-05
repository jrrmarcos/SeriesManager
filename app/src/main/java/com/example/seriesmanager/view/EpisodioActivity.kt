package com.example.seriesmanager.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.seriesmanager.databinding.ActivityEpisodioBinding
import com.example.seriesmanager.model.Episodio
import com.example.seriesmanager.view.EpisodioListaActivity.Extras.EXTRA_EPISODIO
import com.example.seriesmanager.view.EpisodioListaActivity.Extras.EXTRA_POSICAO_EP
import com.example.seriesmanager.view.TemporadaListaActivity.Extras.EXTRA_ID_TEMPORADA

class EpisodioActivity: AppCompatActivity() {
    private val activityEpisodiosBinding: ActivityEpisodioBinding by lazy {
        ActivityEpisodioBinding.inflate(layoutInflater)
    }
    private var posicao = -1;
    private var temporadaId = 0
    private lateinit var episodio: Episodio

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityEpisodiosBinding.root)

        temporadaId = intent.getIntExtra(EXTRA_ID_TEMPORADA, -1)
        posicao = intent.getIntExtra(EXTRA_POSICAO_EP, -1)

        intent.getParcelableExtra<Episodio>(EXTRA_EPISODIO)?.run {
            activityEpisodiosBinding.nomeEt.setText(this.nomeEp)
            activityEpisodiosBinding.duracaoEt.setText(this.duracaoEp.toString())
            activityEpisodiosBinding.numeroSequencialEt.setText(this.numeroSequencialEp.toString())
            activityEpisodiosBinding.assistidoCb.isChecked = this.assistidoEp

            if (posicao == -1) {
                activityEpisodiosBinding.nomeEt.isEnabled = false
                activityEpisodiosBinding.duracaoEt.isEnabled = false
                activityEpisodiosBinding.numeroSequencialEt.isEnabled = false
                activityEpisodiosBinding.assistidoCb.isEnabled = false
                activityEpisodiosBinding.salvarBt.visibility = View.GONE
            }
        }

        activityEpisodiosBinding.salvarBt.setOnClickListener {
            episodio = Episodio(
                activityEpisodiosBinding.numeroSequencialEt.text.toString().toInt(),
                activityEpisodiosBinding.nomeEt.text.toString(),
                activityEpisodiosBinding.duracaoEt.text.toString().toInt(),
                activityEpisodiosBinding.assistidoCb.isChecked,
                temporadaId
            )
            val resultadoIntent = intent.putExtra(EXTRA_EPISODIO, episodio)

            if (posicao != -1) {
                resultadoIntent.putExtra(EXTRA_POSICAO_EP, posicao)
            }
            setResult(RESULT_OK, resultadoIntent)
            finish()
        }
    }
}