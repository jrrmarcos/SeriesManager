package com.example.seriesmanager.adapter

import android.view.*
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.seriesmanager.R
import com.example.seriesmanager.databinding.LayoutEpisodioBinding
import com.example.seriesmanager.model.Episodio
import com.example.seriesmanager.view.OnEpisodioClickListener

class EpisodiosRvAdapter(
    private val onEpisodioClickListener: OnEpisodioClickListener,
    private val episodioList: MutableList<Episodio>
): RecyclerView.Adapter<EpisodiosRvAdapter.EpisodioLayoutHolder>() {

    //Posição que será recuperada pelo menu de contexto
    var posicao: Int = -1

    //View Holder
    inner class EpisodioLayoutHolder(layoutEpisodioBinding: LayoutEpisodioBinding): RecyclerView.ViewHolder(layoutEpisodioBinding.root), View.OnCreateContextMenuListener {
        val nomeEpisodioTv: TextView = layoutEpisodioBinding.nomeEpisodioTv
        val numeroSequencialEpisodioTv: TextView = layoutEpisodioBinding.numeroSequencialEpisodioTv
        val duracaoEpisodioTv: TextView = layoutEpisodioBinding.duracaoEpisodioTv
        val foiVistoCb: CheckBox = layoutEpisodioBinding.assistidoCb

        init {
            itemView.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            view: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            MenuInflater(view?.context).inflate(R.menu.context_menu_episodio, menu)
        }
    }

    //Quando uma nova célula precisa ser criada
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodioLayoutHolder {
        //Criar uma nova célula
        val layoutEpisodioBinding = LayoutEpisodioBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        //Criar um holder associado a nova célula
        return EpisodioLayoutHolder(layoutEpisodioBinding)
    }

    //Quando for necessário atualizar os valores de uma célula
    override fun onBindViewHolder(holder: EpisodioLayoutHolder, position: Int) {
        val episodio = episodioList[position]

        //Atualizar os valores do viewHolder
        with(holder) {
            nomeEpisodioTv.text = episodio.nomeEp
            numeroSequencialEpisodioTv.text = episodio.numeroSequencialEp.toString()
            duracaoEpisodioTv.text = episodio.duracaoEp.toString()
            foiVistoCb.isChecked = episodio.assistidoEp
            itemView.setOnClickListener {
                onEpisodioClickListener.onEpisodioClick(position)
            }
            itemView.setOnLongClickListener{
                posicao = position
                false
            }
        }
    }

    override fun getItemCount(): Int = episodioList.size
}