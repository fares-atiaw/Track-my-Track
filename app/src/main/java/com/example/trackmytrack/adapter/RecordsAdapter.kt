package com.example.trackmytrack.adapter;

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.trackmytrack.data.Record
import com.example.trackmytrack.databinding.ItemLocationBinding

class RecordsAdapter : ListAdapter<Record, RecordsAdapter.MyViewHolder>(DiffRecord()) {

    private var clickListener: ((data: Record) -> Unit)? = null
    fun onClick(listener: (data: Record) -> Unit) {
        clickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.fromInflating(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

    class MyViewHolder private constructor(val binding: ItemLocationBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Record, clickListener: ((data: Record) -> Unit)?)
        {    // use this instead of using transactions for each view here (Binding Adapter).
            binding.x = data    // ‘x’ is the related variable in the XML
            binding.root.setOnClickListener {
                clickListener?.let {
                    it(data)
                }
            }
            binding.executePendingBindings()
        }

        /** private methods**/


        /** static variable(s) or method(s)**/
        companion object {
            fun fromInflating(parent: ViewGroup): MyViewHolder {
                val binding: ItemLocationBinding = ItemLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    class DiffRecord : DiffUtil.ItemCallback<Record>() {
        override fun areItemsTheSame(oldItem: Record, newItem: Record): Boolean =
            (oldItem.id == newItem.id)

        override fun areContentsTheSame(oldItem: Record, newItem: Record): Boolean =
            (oldItem == newItem)
    }

}