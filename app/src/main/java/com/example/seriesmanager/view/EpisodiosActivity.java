package com.example.seriesmanager.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.seriesmanager.databinding.ActivityEpisodiosBinding;
import com.example.seriesmanager.databinding.ActivitySerieBinding;
import com.example.seriesmanager.model.Episodio;
import com.example.seriesmanager.model.Serie;

public class EpisodiosActivity extends AppCompatActivity {
    private ActivityEpisodiosBinding activityEpisodiosBinding;
    private int posicao = -1;
    private Episodio episodio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityEpisodiosBinding = ActivityEpisodiosBinding.inflate(getLayoutInflater());

        setContentView(activityEpisodiosBinding.getRoot());

        activityEpisodiosBinding.salvarBt.setOnClickListener(
                (View view) -> {
                    episodio = new Episodio(
                            Integer.parseInt(activityEpisodiosBinding.numeroEt.getText().toString()),
                            activityEpisodiosBinding.nomeEt.getText().toString(),
                            activityEpisodiosBinding.tempoDuracaoEt.getText().toString()
                    );
                    Intent resultadoIntent = new Intent();
                    resultadoIntent.putExtra(MainActivity.EXTRA_EPISODIO, episodio);

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
        episodio = getIntent().getParcelableExtra(MainActivity.EXTRA_EPISODIO);

        if(episodio!=null){
            activityEpisodiosBinding.numeroEt.setEnabled(false);
            activityEpisodiosBinding.numeroEt.setText(episodio.getNumeroSequencial());
            activityEpisodiosBinding.nomeEt.setText(episodio.getNome());
            activityEpisodiosBinding.tempoDuracaoEt.setText(episodio.getTempoDuracao());

            if(posicao==-1){
                for(int i=0; i<activityEpisodiosBinding.getRoot().getChildCount(); i++){
                    activityEpisodiosBinding.getRoot().getChildAt(i).setEnabled(false);
                }
                activityEpisodiosBinding.salvarBt.setVisibility(View.GONE);
            }
        }
    }
}