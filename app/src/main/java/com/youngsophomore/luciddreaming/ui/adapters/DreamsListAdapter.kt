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
    private var allDreams = listOf<Dream>()
    class ViewHolder(val binding: ItemDreamBinding) : RecyclerView.ViewHolder(binding.root)

    fun setDreams(dreams: List<Dream>){
        Log.d("RecyclerView", "DreamsListAdapter.setDreams()")
        Log.d("RecyclerView", " dreams = ${dreams.joinToString() }}")
        allDreams = dreams
        Log.d("RecyclerView", " allDreams = ${allDreams.joinToString() }}")
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDreamBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("RecyclerView", "DreamsListAdapter.onBindViewHolder()")
        val dream = allDreams[position]
        Log.d("RecyclerView", " ${allDreams[position]} = ${allDreams[position].title}")
        holder.binding.tvDreamsListItemTitle.text = dream.title ?: "null title"
        holder.binding.tvDreamsListItemContent.text = dream.content ?: "NULL CONTENT"
    }

    override fun getItemCount(): Int {
        Log.d("RecyclerView", "allDreams.size = ${allDreams.size}")
        return allDreams.size
    }
}