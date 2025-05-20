package com.youngsophomore.luciddreaming.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.youngsophomore.luciddreaming.databinding.ItemDreamBinding
import com.youngsophomore.luciddreaming.databinding.ItemMetaBinding
import com.youngsophomore.luciddreaming.ui.adapters.DreamsListAdapter.ViewHolder
import com.youngsophomore.luciddreaming.ui.interfaces.MetaItemChooseListener

class MetaListAdapter(val listener: MetaItemChooseListener) :
    ListAdapter<String, MetaListAdapter.ViewHolder>(diffUtil),
    Filterable{
    private var allMetaItems = emptyList<String>()
    private val searchFilter: Filter = object : Filter(){
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            Log.d("Gestures", "MetaListAdapter, performFiltering, $constraint")
            val filteredList = if (constraint.isNullOrEmpty()){
                allMetaItems
            }
            else {
                allMetaItems.filter { it.lowercase().contains(constraint) }
            }
            return FilterResults().apply { values = filteredList }
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            Log.d("Gestures", "MetaListAdapter, publishResults")
            submitList(results?.values as List<String>)
        }

    }

    inner class ViewHolder(val binding: ItemMetaBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                Log.d("Gestures", "MetaListAdapter.ViewHolder, root.setOnClickListener")
                listener.onMetaItemChoose(binding.root.text.toString())
            }
            binding.root.setOnLongClickListener {
                Log.d("Gestures", "MetaListAdapter.ViewHolder, root.setOnLongClickListener")
                listener.onMetaItemDelete(binding.root.text.toString())
                //notifyDataSetChanged()
                true
            }
        }
    }

    fun setMetaItems(metaItems: List<String>){
        allMetaItems = metaItems
        submitList(metaItems)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMetaBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val metaItem = allMetaItems[position]
        holder.binding.root.text = metaItem
    }

    /*override fun getItemCount(): Int {
        return allMetaItems.size
    }*/

    override fun getFilter(): Filter {
        return searchFilter
    }
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<String>() {
            override fun areContentsTheSame(oldItem: String, newItem: String) =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: String, newItem: String) =
                oldItem === newItem
        }
    }
}