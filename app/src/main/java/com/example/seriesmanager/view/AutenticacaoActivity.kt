package com.example.seriesmanager.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.seriesmanager.databinding.ActivityAutenticacaoBinding

class AutenticacaoActivity : AppCompatActivity() {
    private val activityAutenticacaoBinding: ActivityAutenticacaoBinding by lazy {
        ActivityAutenticacaoBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityAutenticacaoBinding.root)
        supportActionBar?.subtitle = "Autenticação"

        with(activityAutenticacaoBinding) {
            cadastrarUsuarioBt.setOnClickListener {
                startActivity(Intent(this@AutenticacaoActivity, CadastrarUsuarioActivity::class.java))
            }

            recuperarSenhaBt.setOnClickListener {
                startActivity(Intent(this@AutenticacaoActivity, RecuperarSenhaActivity::class.java))
            }

            activityAutenticacaoBinding.entrarBt.setOnClickListener {
                val email = emailEt.text.toString()
                val senha = senhaEt.text.toString()
                AutenticacaoFirebase.firebaseAuth.signInWithEmailAndPassword(email, senha).addOnSuccessListener {
                    Toast.makeText(this@AutenticacaoActivity, "Usuário autenticado com sucesso", Toast.LENGTH_SHORT).show()
                    iniciarSerieListactivity()
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this@AutenticacaoActivity, "Usuário/Senha Incorretos", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onStart(){
        super.onStart()
        if(AutenticacaoFirebase.firebaseAuth.currentUser != null){
            iniciarSerieListactivity()
        }
    }

    private fun iniciarSerieListactivity() {
        startActivity(Intent(this@AutenticacaoActivity, SerieListaActivity::class.java))
        finish()
    }
}