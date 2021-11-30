package com.example.seriesmanager.model

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class SerieFirebase: SerieDAO {

    companion object {
        private val BD_SERIES_MANAGER = "series"
    }

    //Referência para o RtDb
    private val  serieRtDb = Firebase.database.getReference(BD_SERIES_MANAGER)

    //Lista de series que simula uma consulta
    private val seriesList: MutableList<Serie> = mutableListOf()
    init {
        serieRtDb.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val novaSerie: Serie? = snapshot.value as? Serie
                novaSerie?.apply {
                    if (seriesList.find { it.nomeSerie == this.nomeSerie } == null) {
                        seriesList.add(this)
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val serieEditada: Serie? = snapshot.value as? Serie
                serieEditada?.apply {
                    seriesList[seriesList.indexOfFirst { it.nomeSerie == this.nomeSerie }] = this
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val serieRemovida: Serie? = snapshot.value as? Serie
                serieRemovida?.apply {
                    seriesList.remove(this)
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                //Não se aplica
            }

            override fun onCancelled(error: DatabaseError) {
                //Não se aplica
            }
        })
        serieRtDb.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                seriesList.clear()
                snapshot.children.forEach {
                    val serie: Serie = it.getValue<Serie>()?: Serie()
                    seriesList.add(serie)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                //Não se aplica
            }
        })
    }
    override fun criarSerie(serie: Serie): Long {
        criarOuAtualizarSerie(serie)
        return 0L
    }

    override fun recuperarSeries(): MutableList<Serie> = seriesList

    override fun removerSerie(nome: String): Int {
        serieRtDb.child(nome).removeValue()
        return 1
    }

    private fun criarOuAtualizarSerie(serie: Serie) {
        serieRtDb.child(serie.nomeSerie).setValue(serie)
    }
}