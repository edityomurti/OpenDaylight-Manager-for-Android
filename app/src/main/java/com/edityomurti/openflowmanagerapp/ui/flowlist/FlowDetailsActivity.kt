package com.edityomurti.openflowmanagerapp.ui.flowlist

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.edityomurti.openflowmanagerapp.R
import com.edityomurti.openflowmanagerapp.models.flowtable.flow.Action
import com.edityomurti.openflowmanagerapp.models.flowtable.flow.Flow
import com.edityomurti.openflowmanagerapp.utils.Constants
import kotlinx.android.synthetic.main.activity_flow_details.*

class FlowDetailsActivity : AppCompatActivity() {

    lateinit var flow: Flow
    lateinit var bundle: Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flow_details)
        title = "Flow Details"
        bundle = intent.extras

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        flow = bundle.getSerializable(Constants.OBJECT_FLOW) as Flow
        showData()
    }

    fun showData(){
        val table = flow.tableId
        val flowId = flow.id
        val flowName = flow.flowName
        val priority = flow.priority
        val hardTimeout = flow.hardTimeOut
        val idleTimeout = flow.idleTimeOut
        val cookie = flow.cookie
        val ethernetType = flow.match?.ethernetMatch?.ethernetType?.type
        val inPort = flow.match?.inPort
        var sourceMac = flow.match?.ethernetMatch?.ethernetSource?.address
        var destMac = flow.match?.ethernetMatch?.ethernetDestination?.address
        var ipSource = flow.match?.ipv4source
        var ipDestination = flow.match?.ipv4destination
        var outputPortData: MutableList<OutputPort> = ArrayList()
        var controllerMaxLength: Int? = null
        var actionDrop = false
        var actionNormalMaxLength: Int? = null
        var actionFlood = false
        var actionFloodAll = false

        var actionData: MutableList<Action>? = ArrayList()
        if(flow.instructions != null && flow.instructions?.instruction!![0].applyActions != null){
            actionData = flow.instructions?.instruction!![0].applyActions?.actionData
            for(i in actionData?.indices!!){
                if(actionData[i].outputAction?.outputNodeConnector.equals("CONTROLLER")){
                    controllerMaxLength = actionData[i].outputAction?.maxLength
                } else if(actionData[i].outputAction?.outputNodeConnector.equals("NORMAL")){
                    actionNormalMaxLength = actionData[i].outputAction?.maxLength
                } else if(actionData[i].outputAction?.outputNodeConnector.equals("FLOOD")){
                    actionFlood = true
                } else if(actionData[i].outputAction?.outputNodeConnector.equals("FLOOD_ALL")){
                    actionFloodAll = true
                } else if(actionData[i].dropAction != null){
                    actionDrop = true
                } else {
                    var outputPort = inPort?.substring(0, inPort.lastIndexOf(":")+1) + actionData[i].outputAction?.outputNodeConnector
                    var outputPortMaxLength = actionData[i].outputAction?.maxLength

                    outputPortData.add(OutputPort(outputPort, outputPortMaxLength))
                }
            }
        } else {
            ll_actions.visibility = View.GONE
        }

        if(table != null){
            tv_table.text = table.toString()
        } else {
            ll_table.visibility = View.GONE
        }

        if(flowId != null){
            tv_id_flow.text = flowId
        } else {
            ll_id.visibility = View.GONE
        }

        if(flowName != null){
            tv_flow_name.text = flowName
        } else {
            ll_flow_name.visibility = View.GONE
        }

        if(priority != null){
            tv_priority.text = priority.toString()
        } else {
            ll_priority.visibility = View.GONE
        }

        if(hardTimeout != null){
            tv_hard_timeout.text = hardTimeout.toString()
        } else {
            ll_hard_timeout.visibility = View.GONE
        }

        if(idleTimeout != null){
            tv_idle_timeout.text = idleTimeout.toString()
        } else {
            ll_idle_timeout.visibility = View.GONE
        }

        if(cookie != null){
            tv_cookie.text = cookie.toString()
        } else {
            ll_cookie.visibility = View.GONE
        }

        if(ethernetType != null){
            tv_ethernet_type.text = ethernetType.toString()
        } else {
            ll_ethernet_type.visibility = View.GONE
        }

        if(inPort != null){
            tv_in_port.text = inPort.toString()
        } else {
            ll_in_port.visibility = View.GONE
        }

        if(sourceMac != null){
            tv_source_mac.text = sourceMac
        } else {
            ll_source_mac.visibility = View.GONE
        }

        if(destMac != null){
            tv_dest_mac.text = destMac
        } else {
            ll_dest_mac.visibility = View.GONE
        }

        if(ipSource != null){
            tv_ip_source.text = ipSource
        } else {
            ll_ip_source.visibility = View.GONE
        }

        if(ipDestination != null){
            tv_ip_dest.text = ipDestination
        } else {
            ll_ip_dest.visibility = View.GONE
        }

        if(controllerMaxLength != null){
            tv_controller_maximum_length.text = controllerMaxLength.toString()
        } else {
            ll_controller.visibility = View.GONE
        }

        if(outputPortData.size != 0){
            tv_output_port.text = outputPortData[0].outputPort
            tv_output_port_maximum_length.text = outputPortData[0].outputMaxLength.toString()

            if(outputPortData.size > 1){
                tv_output_port_2.text = outputPortData[1].outputPort
                tv_output_port_2_maximum_length.text = outputPortData[1].outputMaxLength.toString()
            } else {
                ll_output_port_2.visibility = View.GONE
            }
        } else {
            ll_output_port.visibility = View.GONE
            ll_output_port_2.visibility = View.GONE
        }

        if(actionDrop){
            ll_action_drop.visibility = View.VISIBLE
        } else {
            ll_action_drop.visibility = View.GONE
        }

        if(actionNormalMaxLength != null){
            tv_normal_maximum_length.text = actionNormalMaxLength.toString()
        } else {
            ll_action_normal.visibility = View.GONE
        }

        if(actionFlood){
            ll_action_flood.visibility = View.VISIBLE
        } else {
            ll_action_flood.visibility = View.GONE
        }

        if(actionFloodAll){
            ll_action_flood_all.visibility = View.VISIBLE
        } else {
            ll_action_flood_all.visibility = View.GONE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home -> onBackPressed()
        }
        return true
    }

    data class OutputPort(
            var outputPort: String?,
            var outputMaxLength: Int?
    )
}