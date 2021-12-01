package com.example.seriesmanager.model

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class EpisodioFirebase (temporada: Temporada): EpisodioDAO {

    //Referência para o RtDb
    private val  episodioRtDb = Firebase.database.getReference("Episodios da: " + temporada.nomeSerie + "Temporada : " + temporada.numeroSequencialTemp)

    //Lista de temporadas que simula uma consulta
    private val episodioList: MutableList<Episodio> = mutableListOf()
    init {
        episodioRtDb.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val novaEpisodio: Episodio? = snapshot.value as? Episodio
                novaEpisodio?.apply {
                    if (episodioList.find { it.nomeEp == this.nomeEp } == null) {
                        episodioList.add(this)
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val episodioEditado: Episodio? = snapshot.value as? Episodio
                episodioEditado?.apply {
                    episodioList[episodioList.indexOfFirst { it.nomeEp == this.nomeEp }] = this
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val episodioRemovido: Episodio? = snapshot.value as? Episodio
                episodioRemovido?.apply {
                    episodioList.remove(this)
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                // Não se aplica
            }

            override fun onCancelled(error: DatabaseError) {
                // Não se aplica
            }
        })

        episodioRtDb.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                episodioList.clear()
                snapshot.children.forEach {
                    val episodio: Episodio = it.getValue<Episodio>()?: Episodio()
                    episodioList.add(episodio)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                //Não se aplica
            }
        })

    }

    override fun criarEpisodio(episodio: Episodio): Long {
        criarOuAtualizarEpisodio(episodio)
        return 0L
    }

    override fun recuperarEpisodios(temporadaId: Int): MutableList<Episodio> = episodioList

    override fun recuperarEpisodios(): MutableList<Episodio> = episodioList

    override fun recuperarEpisodio(numeroSequencial: Int, temporadaId: Int): Episodio? {
        return null
    }

    override fun atualizarEpisodio(episodio: Episodio): Int {
        criarOuAtualizarEpisodio(episodio)
        return 1
    }

    override fun removerEpisodio(temporadaId: Int, numeroSequencial: Int): Int {
        //Não se aplica
        return -1
    }

    override fun removerEpisodio(nomeEp: String, numeroSequencialEp: Int): Int {
        episodioRtDb.child("$numeroSequencialEp - $nomeEp").removeValue()
        return 1
    }

    private fun criarOuAtualizarEpisodio(episodio: Episodio) {
        val nodeEpisodio: String = episodio.numeroSequencialEp.toString() + " - " + episodio.nomeEp
        episodioRtDb.child(nodeEpisodio).setValue(episodio)
    }
}