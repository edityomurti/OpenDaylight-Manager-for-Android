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
        val priority = flow.priority
        val hardTimeout = flow.hardTimeOut
        val cookie = flow.cookie
        val ethernetType = flow.match?.ethernetMatch?.ethernetType?.type
        val inPort = flow.match?.inPort
        var controllerMaxLength: Int? = null
        var outputPortData: MutableList<OutputPort> = ArrayList()

        var actionData: MutableList<Action>? = ArrayList()
        if(flow.instructions != null && flow.instructions?.instruction!![0].applyActions != null){
            actionData = flow.instructions?.instruction!![0].applyActions?.actionData
            for(i in actionData?.indices!!){
                if(actionData[i].outputAction?.outputNodeConnector.equals("CONTROLLER")){
                    controllerMaxLength = actionData[i].outputAction?.maxLength
                }
                else {
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