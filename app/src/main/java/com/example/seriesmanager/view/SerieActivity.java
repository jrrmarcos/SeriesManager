package com.example.seriesmanager.view;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.seriesmanager.R;
import com.example.seriesmanager.databinding.ActivitySerieBinding;
import com.example.seriesmanager.model.Serie;

public class SerieActivity extends AppCompatActivity {
    private ActivitySerieBinding activitySerieBinding;
    private int posicao = -1;
    private Serie serie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySerieBinding = ActivitySerieBinding.inflate(getLayoutInflater());

        setContentView(activitySerieBinding.getRoot());

        activitySerieBinding.salvarBt.setOnClickListener(
                (View view) -> {
                    serie = new Serie(
                            activitySerieBinding.nomeEt.getText().toString(),
                            activitySerieBinding.anoLancamentoEt.getText().toString(),
                            activitySerieBinding.emissoraEt.getText().toString(),
                            activitySerieBinding.generoEt.getText().toString()
                    );
                    Intent resultadoIntent = new Intent();
                    resultadoIntent.putExtra(MainActivity.EXTRA_SERIE, serie);

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
        serie = getIntent().getParcelableExtra(MainActivity.EXTRA_SERIE);

        if(serie!=null){
            activitySerieBinding.nomeEt.setEnabled(false);
            activitySerieBinding.nomeEt.setText(serie.getNome());
            activitySerieBinding.anoLancamentoEt.setText(serie.getAno_lancamento());
            activitySerieBinding.emissoraEt.setText(serie.getEmissora());
            activitySerieBinding.generoEt.setText(serie.getGenero());

            if(posicao==-1){
                for(int i=0; i<activitySerieBinding.getRoot().getChildCount(); i++){
                    activitySerieBinding.getRoot().getChildAt(i).setEnabled(false);
                }
                activitySerieBinding.salvarBt.setVisibility(View.GONE);
            }
        }
    }
}