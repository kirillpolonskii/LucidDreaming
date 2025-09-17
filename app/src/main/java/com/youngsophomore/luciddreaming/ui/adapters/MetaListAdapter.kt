package com.youngsophomore.luciddreaming.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.youngsophomore.luciddreaming.databinding.ItemMetaBinding
import com.youngsophomore.luciddreaming.ui.interfaces.MetaItemListener

class MetaListAdapter(val listener: MetaItemListener) :
    ListAdapter<String, MetaListAdapter.ViewHolder>(diffUtil),
    Filterable {
    private var allMetaItems = emptyList<String>()
    private val searchFilter: Filter = object : Filter(){
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList = if (constraint.isNullOrEmpty()){
                allMetaItems
            }
            else {
                allMetaItems.filter { it.lowercase().contains(constraint) }
            }
            return FilterResults().apply { values = filteredList }
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            submitList(results?.values as List<String>)
        }

    }

    inner class ViewHolder(val binding: ItemMetaBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                listener.onChooseMetaItem(binding.root.text.toString())
            }
            binding.root.setOnLongClickListener {
                listener.onDeleteMetaItem(binding.root.text.toString())
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