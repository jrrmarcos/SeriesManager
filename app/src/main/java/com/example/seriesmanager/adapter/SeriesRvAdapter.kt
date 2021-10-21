package com.example.seriesmanager.adapter

import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.seriesmanager.OnSerieClickListener
import com.example.seriesmanager.R
import com.example.seriesmanager.databinding.LayoutSerieBinding
import com.example.seriesmanager.model.Serie

class SeriesRvAdapter (
    private val onSerieClickListener: OnSerieClickListener,
    private val seriesList: MutableList<Serie>
): RecyclerView.Adapter<SeriesRvAdapter.SerieLayoutHolder>() {

    //Posição que será recuperada pelo menu de contexto
    var posicao: Int = -1

    //View Holder
    inner class SerieLayoutHolder(layoutSerieBinding: LayoutSerieBinding): RecyclerView.ViewHolder(layoutSerieBinding.root),
            View.OnCreateContextMenuListener {
                val nomeTv: TextView = layoutSerieBinding.nomeTv
                val anoLancamentoTv: TextView = layoutSerieBinding.anoLancamentoTv
                val emissoraTv: TextView = layoutSerieBinding.emissoraTv

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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SerieLayoutHolder {
        //Criar uma nova célula
        val layoutSerieBinding = LayoutSerieBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        //Criar um holder associado a nova célula
        val viewHolder: SerieLayoutHolder = SerieLayoutHolder(layoutSerieBinding)
        return viewHolder
    }

    //Quando for necessário atualizar os valores de uma célula
    override fun onBindViewHolder(holder: SerieLayoutHolder, position: Int) {
       //Busco a série
        val serie = seriesList[position]

        //Atualizar os valores do viewHolder
        with(holder) {
            nomeTv.text = serie.nome
            anoLancamentoTv.text = serie.ano_lancamento
            emissoraTv.text = serie.emissora

            itemView.setOnClickListener {
                onSerieClickListener.onSerieClick(position)
            }

            itemView.setOnLongClickListener {
                posicao = position
                false
            }
        }
    }

    override fun getItemCount(): Int = seriesList.size
}