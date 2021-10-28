package com.example.seriesmanager.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.seriesmanager.databinding.ActivityEpisodiosBinding;
import com.example.seriesmanager.databinding.ActivityTemporadasBinding;
import com.example.seriesmanager.model.Episodio;
import com.example.seriesmanager.model.Temporada;

public class TemporadasActivity extends AppCompatActivity {
    private ActivityTemporadasBinding activityTemporadasBinding;
    private int posicao = -1;
    private Temporada temporada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityTemporadasBinding = ActivityTemporadasBinding.inflate(getLayoutInflater());

        setContentView(activityTemporadasBinding.getRoot());

        activityTemporadasBinding.salvarBt.setOnClickListener(
                (View view) -> {
                    temporada = new Temporada (
                            activityTemporadasBinding.numeroEt.getText().toString(),
                            activityTemporadasBinding.qtdEpisodiosEt.getText().toString(),
                            activityTemporadasBinding.anoLancamentoEt.getText().toString()
                    );
                    Intent resultadoIntent = new Intent();
                    resultadoIntent.putExtra(MainActivity.EXTRA_TEMPORADA, temporada);

                    //Se foi edição, também devolver a posição
                    if(posicao!=-1){
                        resultadoIntent.putExtra(MainActivity.EXTRA_POSICAO, posicao);
                    }

                    setResult(RESULT_OK, resultadoIntent);
                    finish();
                }
        );

        //Verificando se é uma edição ou consulta e preenchimento de campos
        posicao = getIntent().getIntExtra(MainActivity.EXTRA_POSICAO, -1);
        temporada = getIntent().getParcelableExtra(MainActivity.EXTRA_TEMPORADA);

        if(temporada!=null){
            activityTemporadasBinding.numeroEt.setEnabled(false);
            activityTemporadasBinding.numeroEt.setText(temporada.getNumeroSequencialTemp());
            activityTemporadasBinding.anoLancamentoEt.setText(temporada.getAnoLancamentoTemp());
            activityTemporadasBinding.qtdEpisodiosEt.setText(temporada.getQtdEpisodiosTemp());

            if(posicao==-1){
                for(int i=0; i<activityTemporadasBinding.getRoot().getChildCount(); i++){
                    activityTemporadasBinding.getRoot().getChildAt(i).setEnabled(false);
                }
                activityTemporadasBinding.salvarBt.setVisibility(View.GONE);
            }
        }
    }
}