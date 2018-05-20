package com.edityomurti.openflowmanagerapp.ui.topology

import android.animation.ObjectAnimator
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.edityomurti.openflowmanagerapp.R
import com.edityomurti.openflowmanagerapp.models.topology.Link
import com.edityomurti.openflowmanagerapp.models.topology.NetworkTopology
import com.edityomurti.openflowmanagerapp.models.topology.Node
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter
import com.github.aakira.expandablelayout.Utils
import kotlinx.android.synthetic.main.item_device.view.*
import kotlinx.android.synthetic.main.item_node_link.view.*

class DeviceListAdapter(private var networkTopology: NetworkTopology?): RecyclerView.Adapter<DeviceListAdapter.DeviceListViewHolder>(){
    var switchNodeData: MutableList<Node> = ArrayList()
    var hostNodeData: MutableList<Node> = ArrayList()
    var linkData: MutableList<Link> = ArrayList()

    var expandState: SparseBooleanArray = SparseBooleanArray()

    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_device, parent, false)
        context = parent.context
        return DeviceListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return switchNodeData.size + hostNodeData.size
    }

    override fun onBindViewHolder(holder: DeviceListViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        if(position<switchNodeData.size){
            var deviceName = switchNodeData[position].nodeId

            if (position == 0 ){
                holder.itemView.tv_device_type.visibility = View.VISIBLE
                holder.itemView.tv_device_type.text = "Switches"
            } else {
                holder.itemView.tv_device_type.visibility = View.GONE
            }
            holder.itemView.iv_device.setImageResource(R.drawable.ic_switch)
            holder.itemView.tv_device_name.text = deviceName
            holder.itemView.tv_device_node.text = "${switchNodeData[position].terminationPointData.size-1}  nodes"

            for(i in linkData.indices){
                if(linkData.get(i).source?.sourceNode.equals(deviceName)){
                    var linkView = LayoutInflater.from(context).inflate(R.layout.item_node_link, null, false)
                    linkView.tv_host_name.text = linkData.get(i).destination?.destNode
                    linkView.tv_port_number.text = linkData.get(i).source?.sourceTp?.substring(linkData.get(i).source?.sourceTp?.lastIndexOf(":")!!+1)
                    holder.itemView.expandable_layout.addView(linkView)
                }
            }
        } else {
            var deviceName = hostNodeData[position-switchNodeData.size].nodeId
            if(position-switchNodeData.size == 0){
                holder.itemView.tv_device_type.visibility = View.VISIBLE
                holder.itemView.tv_device_type.text = "Hosts"
            } else {
                holder.itemView.tv_device_type.visibility = View.GONE
            }
            holder.itemView.iv_device.setImageResource(R.drawable.ic_laptop)
            holder.itemView.tv_device_name.text = deviceName
            holder.itemView.tv_device_node.text = hostNodeData[position-switchNodeData.size].hostTrackerAddressesData[0].ip
            for(i in linkData.indices){
                if(linkData.get(i).source?.sourceNode.equals(deviceName)){
                    var linkView = LayoutInflater.from(context).inflate(R.layout.item_node_link, null, false)
                    linkView.tv_host_name.text = linkData.get(i).destination?.destNode
                    linkView.tv_via.visibility = View.GONE
                    linkView.tv_port_number.visibility = View.GONE
                    holder.itemView.expandable_layout.addView(linkView)
                }
            }
        }

        holder.itemView.expandable_layout.setInRecyclerView(true)
        val interpolator = Utils.createInterpolator(Utils.FAST_OUT_LINEAR_IN_INTERPOLATOR)
        holder.itemView.expandable_layout.setInterpolator(interpolator)
        if(!expandState[position]){
            holder.itemView.expandable_layout.post {
                holder.itemView.expandable_layout.collapse(-1, null)
            }
        } else {

        }

        holder.itemView.expandable_layout.setListener(object : ExpandableLayoutListenerAdapter(){
            override fun onPreOpen() {
                createRotateAnimator(holder.itemView.view_arrow, 0f, 180f).start()
                expandState.put(position, true)
            }

            override fun onPreClose() {
                createRotateAnimator(holder.itemView.view_arrow, 180f, 0f).start()
                expandState.put(position, false)
            }
        })

        if (expandState[position]) {
            holder.itemView.view_arrow.rotation = 180f
        } else {
            holder.itemView.view_arrow.rotation = 0f
        }

        holder.itemView.cv_device.setOnClickListener{
            holder.itemView.expandable_layout.toggle()
        }

        println("device $position expanded ${holder.itemView.expandable_layout.isExpanded}")
    }

    inner class DeviceListViewHolder(view: View): RecyclerView.ViewHolder(view)

    fun setData(data: NetworkTopology){
        networkTopology = data
        switchNodeData.clear()
        hostNodeData.clear()
        var nodesData = networkTopology?.topologyData?.topology?.get(0)?.nodeData
        for(i in nodesData?.indices!!){
            if(nodesData.get(i).hostTrackerId == null){
                switchNodeData.add(nodesData[i])
            } else {
                hostNodeData.add(nodesData[i])
            }
        }

        for(i in 0..(switchNodeData.size + hostNodeData.size)){
            expandState.append(i, false)
        }

        linkData.clear()
        linkData = networkTopology?.topologyData?.topology?.get(0)?.linkData!!
        notifyDataSetChanged()
    }

    fun createRotateAnimator(target: View, from: Float, to: Float): ObjectAnimator{
        var animator = ObjectAnimator.ofFloat(target, "rotation", from, to)
        animator.duration = 300
        animator.interpolator = Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR)
        return animator
    }
}