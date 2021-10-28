package com.example.seriesmanager.adapter

import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.seriesmanager.R
import com.example.seriesmanager.databinding.LayoutEpisodioBinding
import com.example.seriesmanager.model.Episodio
import com.example.seriesmanager.view.EpisodioListaActivity

class EpisodiosRvAdapter(
    private val onEpisodiosClickListener: EpisodioListaActivity,
    private val episodiosList: MutableList<Episodio>
    ): RecyclerView.Adapter<EpisodiosRvAdapter.EpisodioLayoutHolder>() {

        //Posição que será recuperada pelo menu de contexto
        var posicao: Int = -1

        //View Holder
        inner class EpisodioLayoutHolder(layoutEpisodioBinding: LayoutEpisodioBinding): RecyclerView.ViewHolder(layoutEpisodioBinding.root),
            View.OnCreateContextMenuListener {
            val numeroTv: TextView = layoutEpisodioBinding.numeroTv
            val nomeTv: TextView = layoutEpisodioBinding.nomeTv
            val tempoDuracaoTv: TextView = layoutEpisodioBinding.tempoDuracaoTv

            init {
                itemView.setOnCreateContextMenuListener(this)
            }

            override fun onCreateContextMenu(
                menu: ContextMenu?,
                view: View?,
                menuInfo: ContextMenu.ContextMenuInfo?
            ) {
                MenuInflater(view?.context).inflate(R.menu.context_manu_main, menu)
            }
        }

        //Quando uma nova célula precisa ser criada
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodioLayoutHolder {
            //Criar uma nova célula
            val layoutEpisodioBinding = LayoutEpisodioBinding.inflate(LayoutInflater.from(parent.context), parent, false)

            //Criar um holder associado a nova célula
            val viewHolder: EpisodiosRvAdapter.EpisodioLayoutHolder = EpisodioLayoutHolder(layoutEpisodioBinding)
            return viewHolder
        }

        //Quando for necessário atualizar os valores de uma célula
        override fun onBindViewHolder(holder: EpisodioLayoutHolder, position: Int) {
            //Busco a série
            val episodio = episodiosList[position]
            //val textAssistido = if(episodio.assistidoEp.toString() == "true") "Assistido" else "Não Assistido"

            //Atualizar os valores do viewHolder
            with(holder) {
                numeroTv.text = episodio.numeroSequencialEp
                nomeTv.text = episodio.nomeEp
                tempoDuracaoTv.text = episodio.tempoDuracaoEp
                //assistidoTv.text = episodio.assistidoEp

                itemView.setOnClickListener {
                    onEpisodiosClickListener.onEpisodioClick(position)
                }

                itemView.setOnLongClickListener {
                    posicao = position
                    false
                }
            }
        }

        override fun getItemCount(): Int = episodiosList.size
    }