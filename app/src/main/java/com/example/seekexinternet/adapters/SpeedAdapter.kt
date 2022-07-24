package com.example.seekexinternet.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.seekexinternet.R
import com.example.seekexinternet.models.InternetSpeedModel
import kotlinx.android.synthetic.main.internet_speed_list_des.view.*

class SpeedAdapter: RecyclerView.Adapter<SpeedAdapter.VH>() {

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differList = object : DiffUtil.ItemCallback<InternetSpeedModel>(){

        override fun areItemsTheSame(
            oldItem: InternetSpeedModel,
            newItem: InternetSpeedModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: InternetSpeedModel,
            newItem: InternetSpeedModel
        ): Boolean {
            return oldItem == newItem
        }
    }

    val speedList = AsyncListDiffer(this, differList)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.internet_speed_list_des, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val model = speedList.currentList[position]
        holder.itemView.apply {
            tv_speed.text = model.speed
            tv_time.text = model.time
        }
    }

    override fun getItemCount(): Int {
        return speedList.currentList.size
    }
}