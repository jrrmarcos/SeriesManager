package com.example.seriesmanager.adapter

import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.seriesmanager.OnTemporadaClickListener
import com.example.seriesmanager.R
import com.example.seriesmanager.databinding.LayoutTemporadaBinding
import com.example.seriesmanager.model.Temporada

class TemporadasRvAdapter (
    private val onTemporadasClickListener: OnTemporadaClickListener,
    private val temporadasList: MutableList<Temporada>
    ): RecyclerView.Adapter<TemporadasRvAdapter.TemporadaLayoutHolder>() {

        //Posição que será recuperada pelo menu de contexto
        var posicao: Int = -1

        //View Holder
        inner class TemporadaLayoutHolder(layoutTemporadaBinding: LayoutTemporadaBinding): RecyclerView.ViewHolder(layoutTemporadaBinding.root),
            View.OnCreateContextMenuListener {
            val numeroTv: TextView = layoutTemporadaBinding.numeroTv
            val qtdEpisodioTv: TextView = layoutTemporadaBinding.qtdEpisodiosTv
            val anoLancamentoTv: TextView = layoutTemporadaBinding.anoLancamentoTv

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
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TemporadaLayoutHolder {
            //Criar uma nova célula
            val layoutTemporadaBinding = LayoutTemporadaBinding.inflate(LayoutInflater.from(parent.context), parent, false)

            //Criar um holder associado a nova célula
            val viewHolder: TemporadaLayoutHolder = TemporadaLayoutHolder(layoutTemporadaBinding)
            return viewHolder
        }

        //Quando for necessário atualizar os valores de uma célula
        override fun onBindViewHolder(holder: TemporadaLayoutHolder, position: Int) {
            //Busco a série
            val temporada = temporadasList[position]

            //Atualizar os valores do viewHolder
            with(holder) {
                numeroTv.text = temporada.numeroSequencialTemp
                qtdEpisodioTv.text = temporada.qtdEpisodiosTemp
                anoLancamentoTv.text = temporada.anoLancamentoTemp

                itemView.setOnClickListener {
                    onTemporadasClickListener.onTemporadaClick(position)
                }

                itemView.setOnLongClickListener {
                    posicao = position
                    false
                }
            }
        }

        override fun getItemCount(): Int = temporadasList.size
    }
