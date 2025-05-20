package com.youngsophomore.luciddreaming.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.youngsophomore.luciddreaming.databinding.ItemDreamBinding
import com.youngsophomore.luciddreaming.databinding.ItemMetaBinding
import com.youngsophomore.luciddreaming.ui.adapters.DreamsListAdapter.ViewHolder
import com.youngsophomore.luciddreaming.ui.interfaces.MetaItemChooseListener

class MetaListAdapter(val listener: MetaItemChooseListener) : RecyclerView.Adapter<MetaListAdapter.ViewHolder>() {
    private var allMetaItems = emptyList<String>()
    inner class ViewHolder(val binding: ItemMetaBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                Log.d("Gestures", "MetaListAdapter.ViewHolder, root.setOnClickListener")
                listener.onMetaItemChoose(binding.root.text.toString())
            }
            binding.root.setOnLongClickListener {
                Log.d("Gestures", "MetaListAdapter.ViewHolder, root.setOnLongClickListener")
                listener.onMetaItemDelete(binding.root.text.toString())
                true
            }
        }
    }

    fun setMetaItems(metaItems: List<String>){
        allMetaItems = metaItems
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMetaBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val metaItem = allMetaItems[position]
        holder.binding.root.text = metaItem
    }

    override fun getItemCount(): Int {
        return allMetaItems.size
    }
}