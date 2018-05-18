package com.edityomurti.openflowmanagerapp.ui.flowlist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.edityomurti.openflowmanagerapp.R
import com.edityomurti.openflowmanagerapp.models.flowtable.flow.Flow
import kotlinx.android.synthetic.main.item_flow.view.*

class FlowListAdapter(private var dataList: MutableList<Flow>): RecyclerView.Adapter<FlowListAdapter.FlowListViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlowListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_flow, parent, false)

        return FlowListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: FlowListViewHolder, position: Int) {
        holder.itemView.tv_id_node.text = dataList[position].nodeId.toString()
        holder.itemView.tv_id_flow.text = dataList[position].id.toString()
        holder.itemView.tv_id_table.text = "table:" + dataList[position].tableId.toString()
        holder.itemView.tv_priority.text = dataList[position].priority.toString()

        holder.itemView.card_flow_item.setOnClickListener {  }
    }

    inner class FlowListViewHolder(view: View): RecyclerView.ViewHolder(view)

    fun setData(dataList: MutableList<Flow>){
        this.dataList.clear()
        this.dataList.addAll(dataList)
//        this.dataList.sortBy { it.nodeId }
        this.dataList.sortWith(compareBy({it.nodeId}))
        notifyDataSetChanged()
    }

    fun addData(dataList: MutableList<Flow>){
        this.dataList.addAll(dataList)
//        this.dataList.sortBy { it.nodeId }

        this.dataList.sortWith(compareBy({it.nodeId}, {it.priority}))
        this.dataList.reverse()
        notifyDataSetChanged()
    }
}