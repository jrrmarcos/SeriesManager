package com.example.seriesmanager.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.seriesmanager.databinding.ActivityTemporadaBinding
import com.example.seriesmanager.model.Serie
import com.example.seriesmanager.model.Temporada
import com.example.seriesmanager.view.SerieListaActivity.Extras.EXTRA_SERIE
import com.example.seriesmanager.view.TemporadaListaActivity.Extras.EXTRA_POSICAO_TEMP
import com.example.seriesmanager.view.TemporadaListaActivity.Extras.EXTRA_TEMPORADA

class TemporadaActivity: AppCompatActivity() {
    private val activityTemporadaBinding: ActivityTemporadaBinding by lazy {
        ActivityTemporadaBinding.inflate(layoutInflater)
    }
    private var posicao = -1;
    private lateinit var serie: Serie
    private lateinit var temporada: Temporada

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityTemporadaBinding.root)

        //Inicializar a lateinit var
        serie = intent.getParcelableExtra<Serie>(EXTRA_SERIE)!!

        //Visualizar temporada ou adicionar um nova
        posicao = intent.getIntExtra(EXTRA_POSICAO_TEMP, -1)
        intent.getParcelableExtra<Temporada>(EXTRA_TEMPORADA)?.apply {
            activityTemporadaBinding.numeroSequencialEpisodioEt.setText(this.numeroSequencialTemp.toString())
            activityTemporadaBinding.anoLancamentoEt.setText(this.anoLancamentoTemp)
            activityTemporadaBinding.qtdEpisodiosEt.setText(this.qtdEpisodiosTemp)
            if (posicao != -1) {
                activityTemporadaBinding.numeroSequencialEpisodioEt.isEnabled = false
                activityTemporadaBinding.anoLancamentoEt.isEnabled = false
                activityTemporadaBinding.qtdEpisodiosEt.isEnabled = false
                activityTemporadaBinding.salvarBt.visibility = View.GONE
            }
        }

        activityTemporadaBinding.salvarBt.setOnClickListener {
            temporada = Temporada(
                activityTemporadaBinding.numeroSequencialEpisodioEt.text.toString().toInt(),
                activityTemporadaBinding.anoLancamentoEt.text.toString(),
                activityTemporadaBinding.qtdEpisodiosEt.text.toString(),
                serie.nomeSerie
            )
            val resultadoIntent = intent.putExtra(EXTRA_TEMPORADA, temporada)
            if (posicao != -1) {
                resultadoIntent.putExtra(EXTRA_TEMPORADA, posicao)
            }
            setResult(AppCompatActivity.RESULT_OK, resultadoIntent)
            finish()
        }
    }
}