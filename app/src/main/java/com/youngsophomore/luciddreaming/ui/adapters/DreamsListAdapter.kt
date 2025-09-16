package com.youngsophomore.luciddreaming.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.youngsophomore.luciddreaming.data.model.Dream
import com.youngsophomore.luciddreaming.databinding.ItemDreamBinding

class DreamsListAdapter : RecyclerView.Adapter<DreamsListAdapter.ViewHolder>() {
    private var allDreams = listOf<Dream>()
    lateinit var listener: DreamClickListener

    inner class ViewHolder(val binding: ItemDreamBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                listener.onChooseDream(allDreams[adapterPosition].id)
            }
            binding.root.setOnLongClickListener {
                listener.onDeleteDream(allDreams[adapterPosition])
                true
            }
        }
    }

    fun setDreams(dreams: List<Dream>){
        allDreams = dreams
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDreamBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dream = allDreams[position]
        holder.binding.tvDreamsListItemTitle.text = dream.title
        holder.binding.tvDreamsListItemContent.text = dream.content
    }

    override fun getItemCount(): Int {
        return allDreams.size
    }

    interface DreamClickListener {
        fun onChooseDream(id: Int)
        fun onDeleteDream(dream: Dream)
    }
}