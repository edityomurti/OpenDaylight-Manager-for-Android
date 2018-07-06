package com.edityomurti.openflowmanagerapp.ui.topology

import android.animation.ObjectAnimator
import android.content.Context
import android.provider.SyncStateContract
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.edityomurti.openflowmanagerapp.R
import com.edityomurti.openflowmanagerapp.models.topology.Device
import com.edityomurti.openflowmanagerapp.models.topology.Link
import com.edityomurti.openflowmanagerapp.models.topology.NetworkTopology
import com.edityomurti.openflowmanagerapp.models.topology.Node
import com.edityomurti.openflowmanagerapp.utils.Constants
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter
import com.github.aakira.expandablelayout.Utils
import kotlinx.android.synthetic.main.item_device.view.*
import kotlinx.android.synthetic.main.item_node_link.view.*

class DeviceListAdapter(private var deviceData: MutableList<Device>, private var deviceColorList: MutableList<Int>): RecyclerView.Adapter<DeviceListAdapter.DeviceListViewHolder>(){
    var expandState: SparseBooleanArray = SparseBooleanArray()

    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_device, parent, false)
        context = parent.context
        return DeviceListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return deviceData.size
    }

    override fun onBindViewHolder(holder: DeviceListViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        if(deviceData[position]?.deviceType.equals(Constants.DEVICE_TYPE_SWITCH)){
            if (position == 0 ){
                holder.itemView.tv_device_type.visibility = View.VISIBLE
                holder.itemView.tv_device_type.text = "Switches"
            } else {
                holder.itemView.tv_device_type.visibility = View.GONE
            }
            holder.itemView.iv_device.setImageResource(R.drawable.ic_switch_new)
            var deviceName = deviceData[position].deviceName
            holder.itemView.tv_device_name.text = deviceName
            holder.itemView.tv_device_node.text = deviceData[position].deviceDesc


            if(deviceData[position].linkDevice.size != 0 && deviceData[position].linkDevice.size != null){
                var linkDevice = deviceData[position].linkDevice

                for(i in linkDevice.indices){
                    var linkView = LayoutInflater.from(context).inflate(R.layout.item_node_link, null, false)
                    var hostName = linkDevice.get(i).destination?.destNode
                    linkView.tv_host_name.text = hostName
                    if(hostName!!.contains("host")){
                        try {
                            linkView.tv_host_name.setTextColor(deviceColorList.get(hostName.get(18).toString().toInt()))
                        } catch (e: Exception){
                            Log.e("Failed assign color,", e.toString())
                        }
                    } else {
                        linkView.tv_host_name.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryLight))
                    }
                    println("link device i : $i ,pos : $position = ${linkDevice.get(i).destination?.destNode}")

                    linkView.tv_port_number.text = linkDevice.get(i).source?.sourceTp?.substring(linkDevice.get(i).source?.sourceTp?.lastIndexOf(":")!!+1)
                    holder.itemView.expandable_layout.addView(linkView)
                }
            } else {
                var linkView = LayoutInflater.from(context).inflate(R.layout.item_node_link, null, false)
                linkView.tv_host_name.text = "No flows detected"
                holder.itemView.expandable_layout.addView(linkView)
            }

        } else {
            if(deviceData[position-1].deviceType.equals(Constants.DEVICE_TYPE_SWITCH)){
                holder.itemView.tv_device_type.visibility = View.VISIBLE
                holder.itemView.tv_device_type.text = "Hosts"
            } else {
                holder.itemView.tv_device_type.visibility = View.GONE
            }
            holder.itemView.iv_device.setImageResource(R.drawable.ic_laptop_new)
            var deviceName = deviceData[position].deviceName
            holder.itemView.tv_device_name.text = deviceName
            try{
                holder.itemView.tv_device_name.setTextColor(deviceColorList.get(deviceName?.get(18).toString().toInt()))
            } catch (e: Exception){
                Log.e("Failed assign color, ", e.toString())
            }


            holder.itemView.tv_device_node.text = deviceData[position].deviceDesc
            var linkDevice = deviceData[position].linkDevice

            if(deviceData[position].linkDevice.size != 0 && deviceData[position].linkDevice.size != null) {
                for(i in linkDevice.indices){
                    var linkView = LayoutInflater.from(context).inflate(R.layout.item_node_link, null, false)
                    linkView.tv_host_name.text = linkDevice.get(i).destination?.destNode
                    linkView.tv_port_number.text = linkDevice.get(i).source?.sourceTp?.substring(linkDevice.get(i).source?.sourceTp?.lastIndexOf(":")!!+1)
                    linkView.tv_via.visibility = View.GONE
                    linkView.tv_port_number.visibility = View.GONE
                    holder.itemView.expandable_layout.addView(linkView)
                }
            } else {
                var linkView = LayoutInflater.from(context).inflate(R.layout.item_node_link, null, false)
                linkView.tv_host_name.text = "No flows detected"
                holder.itemView.expandable_layout.addView(linkView)
            }
        }

        holder.itemView.expandable_layout.setInRecyclerView(true)
        val interpolator = Utils.createInterpolator(Utils.FAST_OUT_LINEAR_IN_INTERPOLATOR)
        holder.itemView.expandable_layout.setInterpolator(interpolator)
        if(!expandState[position]){
            holder.itemView.expandable_layout.post {
//                holder.itemView.expandable_layout.collapse(-1, null)
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

    fun setData(data: MutableList<Device>){
        deviceData.clear()
        deviceData.addAll(data)
        notifyDataSetChanged()
    }

    fun createRotateAnimator(target: View, from: Float, to: Float): ObjectAnimator{
        var animator = ObjectAnimator.ofFloat(target, "rotation", from, to)
        animator.duration = 300
        animator.interpolator = Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR)
        return animator
    }
}