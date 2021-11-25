package com.example.seriesmanager.adapter

import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.seriesmanager.R
import com.example.seriesmanager.databinding.LayoutSerieBinding
import com.example.seriesmanager.model.Serie
import com.example.seriesmanager.view.SerieListaActivity

class SeriesRvAdapter (
    private val onSerieClickListener: SerieListaActivity,
    private val serieList: MutableList<Serie>
): RecyclerView.Adapter<SeriesRvAdapter.SerieLayoutHolder>() {

    //Posição que será recuperada pelo menu de contexto
    var posicao: Int = -1

    //View Holder
    inner class SerieLayoutHolder(layoutSerieBinding: LayoutSerieBinding): RecyclerView.ViewHolder(layoutSerieBinding.root), View.OnCreateContextMenuListener {
        val nomeTv: TextView = layoutSerieBinding.nomeTv
        val anoLancamentoTv: TextView = layoutSerieBinding.anoLancamentoTv
        val emissoraTv: TextView = layoutSerieBinding.emissoraTv
        val generoTv: TextView = layoutSerieBinding.generoTv

        init {
            itemView.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            view: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            MenuInflater(view?.context).inflate(R.menu.context_menu_serie, menu)
        }
    }

    //Quando uma nova célula precisa ser criada
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SerieLayoutHolder {
        //Criar uma nova célula
        val layoutSerieBinding = LayoutSerieBinding.inflate(LayoutInflater.from(parent.context), parent,false)

        //Criar um holder associado a nova célula
        return SerieLayoutHolder(layoutSerieBinding)
    }

    //Quando for necessário atualizar os valores de uma célula
    override fun onBindViewHolder(holder: SerieLayoutHolder, position: Int) {
        //Buscar a série
        val serie = serieList[position]

        //Atualizar os valores do viewHolder
        with(holder) {
            nomeTv.text = serie.nomeSerie
            anoLancamentoTv.text = serie.anoLancamentoSerie
            emissoraTv.text = serie.emissoraSerie
            generoTv.text = serie.generoSerie
            itemView.setOnClickListener {
                onSerieClickListener.onSerieClick(position)
            }
            itemView.setOnLongClickListener{
                posicao = position
                false
            }
        }
    }

    override fun getItemCount(): Int = serieList.size
}
