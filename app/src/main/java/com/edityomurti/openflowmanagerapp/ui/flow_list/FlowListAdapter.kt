package com.edityomurti.openflowmanagerapp.ui.flow_list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.edityomurti.openflowmanagerapp.R
import com.edityomurti.openflowmanagerapp.models.flowtable.flow.Flow
import com.edityomurti.openflowmanagerapp.models.topology.NodeDataSerializable
import com.edityomurti.openflowmanagerapp.ui.flow_details.FlowDetailsActivity
import com.edityomurti.openflowmanagerapp.utils.Constants
import kotlinx.android.synthetic.main.item_flow.view.*

class FlowListAdapter(private var context: Context,
                      private var fragment: Fragment,
                      private var dataList: MutableList<Flow>,
                      private var nodeList: ArrayList<String>,
                      private var nodeData: NodeDataSerializable?): RecyclerView.Adapter<FlowListAdapter.FlowListViewHolder>(){

    var isStatisticShown = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlowListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_flow, parent, false)

        return FlowListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: FlowListViewHolder, position: Int) {
        println("isStatisticShown : $isStatisticShown")
        if(isStatisticShown){
            holder.itemView.layout_statistic.visibility = View.VISIBLE
        } else {
            holder.itemView.layout_statistic.visibility = View.GONE
        }

        holder.itemView.tv_id_node.text = dataList[position].nodeId.toString()
        holder.itemView.tv_id_flow.text = dataList[position].id.toString()
        holder.itemView.tv_flow_type.text = dataList[position].flowType
        holder.itemView.tv_priority.text = dataList[position].priority.toString()
        holder.itemView.tv_packet_count.text = dataList[position].flowStatistics?.packetCount.toString()
        holder.itemView.tv_byte_count.text = dataList[position].flowStatistics?.byteCount.toString()

        holder.itemView.card_flow_item.setOnClickListener { openDetailsFlow(dataList[position], position) }
    }

    inner class FlowListViewHolder(view: View): RecyclerView.ViewHolder(view)

    fun openDetailsFlow(flow: Flow, position: Int){
        val bundle = Bundle()
        bundle.putSerializable(Constants.OBJECT_FLOW, flow)
        val intent = Intent(context, FlowDetailsActivity::class.java)
        if(flow.flowType == Constants.DATA_TYPE_CONFIG){
            var extras = Bundle()
            extras.putSerializable(Constants.NODE_DATA, nodeData)
            extras.putInt(Constants.FLOW_POSITION, position)
            intent.putExtras(extras)
            intent.putStringArrayListExtra(Constants.NODE_LIST, nodeList)
        } else {

        }
        intent.putExtras(bundle)
        println("onActivityResult, startActivityForResult FlowListAdapter ")
        fragment.startActivityForResult(intent, Constants.RequestCode.OPEN_DETAILS_ACTIVITY)
    }

    fun setData(dataList: MutableList<Flow>){
        this.dataList.clear()
        this.dataList.addAll(dataList)
        this.dataList.sortWith(compareBy({it.nodeId}))
        notifyDataSetChanged()
    }

    fun addData(dataList: MutableList<Flow>,
                nodeList: ArrayList<String>,
                nodeData: NodeDataSerializable?){
        this.nodeList = nodeList
        this.nodeData = nodeData
        this.dataList.addAll(dataList)

        this.dataList.sortWith(compareBy({it.nodeId}, {it.priority}))
        this.dataList.reverse()
        this.dataList.sortWith(compareByDescending { it.flowType })
        notifyDataSetChanged()
    }

    fun showStatistic(isShown: Boolean){
        this.isStatisticShown = isShown
        notifyDataSetChanged()
    }

    fun clearData(){
        this.dataList.clear()
    }
}