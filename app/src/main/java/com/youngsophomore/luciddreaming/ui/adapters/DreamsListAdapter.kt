package com.youngsophomore.luciddreaming.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.youngsophomore.luciddreaming.ui.viewmodels.DreamsListViewModel
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.youngsophomore.luciddreaming.data.model.Dream
import com.youngsophomore.luciddreaming.databinding.ItemDreamBinding
import javax.inject.Inject

class DreamsListAdapter : RecyclerView.Adapter<DreamsListAdapter.ViewHolder>() {
    var allDreams = emptyList<Dream>()
    class ViewHolder(val binding: ItemDreamBinding) : RecyclerView.ViewHolder(binding.root)

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
        holder.binding.tvDreamsListItemTitle.text = dream.title ?: "null title"
        holder.binding.tvDreamsListItemContent.text = dream.content ?: "NULL CONTENT"
    }

    override fun getItemCount(): Int {
        //Log.d("RecyclerView", "allDreams.size = ${allDreams.size}")
        return allDreams.size
    }
}