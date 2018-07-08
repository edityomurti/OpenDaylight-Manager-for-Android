package com.edityomurti.openflowmanagerapp.ui.flow_add


import android.app.AlertDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner

import com.edityomurti.openflowmanagerapp.R
import com.edityomurti.openflowmanagerapp.models.FlowProperties
import com.edityomurti.openflowmanagerapp.models.flowtable.flow.*
import com.edityomurti.openflowmanagerapp.models.topology.NodeDataSerializable
import com.edityomurti.openflowmanagerapp.utils.Constants
import kotlinx.android.synthetic.main.fragment_add_flow_action.view.*
import kotlinx.android.synthetic.main.layout_action_controller.view.*
import kotlinx.android.synthetic.main.layout_action_drop.view.*
import kotlinx.android.synthetic.main.layout_action_flood.view.*
import kotlinx.android.synthetic.main.layout_action_flood_all.view.*
import kotlinx.android.synthetic.main.layout_action_normal.*
import kotlinx.android.synthetic.main.layout_action_normal.view.*
import kotlinx.android.synthetic.main.layout_action_output_port.view.*
import kotlinx.android.synthetic.main.layout_action_output_port_2.view.*

class AddFlowActionFragment : Fragment() {

    lateinit var mView: View

    var actionData: MutableList<FlowProperties> = ArrayList()
    var selectedActionData: MutableList<FlowProperties> = ArrayList()

    val tag_action_drop = "action_drop"
    val tag_action_flood = "action_flood"
    val tag_action_flood_all = "action_flood_all"
    val tag_action_controller = "action_controller"
    val tag_action_normal = "action_normal"
    val tag_action_output_port = "action_output_port"
    val tag_action_output_port_2 = "action_output_port_2"

    var nodeData: NodeDataSerializable? = null
    var newFlow: Flow? = null

    var arrayAdapterPort: ArrayAdapter<String>? = null

    val VALUE_MAX = 65535
    val VALUE_CANT_BE_BLANK = "Cannot be blank"
    val VALUE_ERROR = "Value must between 0 - $VALUE_MAX"

    var modeAdd = true
    var inflater: LayoutInflater? = null

    companion object {
        fun newInstance(nodeData: NodeDataSerializable): AddFlowActionFragment{
            val args = Bundle()
            args.putSerializable(Constants.NODE_DATA, nodeData)

            var fragment = AddFlowActionFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_add_flow_action, container, false)
        this.inflater = inflater

        newFlow = (activity as AddFlowActivity).getFlow()

        if(arguments != null){
            var nodeList: ArrayList<String>? = null

            nodeData = arguments!!.getSerializable(Constants.NODE_DATA) as NodeDataSerializable
            for(i in nodeData!!.nodeSerializableData.indices){
                if(nodeData!!.nodeSerializableData.get(i).nodeId == newFlow?.nodeId){
                    nodeList = nodeData!!.nodeSerializableData.get(i).nodeConnector
                }
            }
            arrayAdapterPort = ArrayAdapter(context, R.layout.item_spinner, nodeList)
        }

        setDefaultData()
        setData()

        var alertDialog = AlertDialog.Builder(context)
        var arrayAdapter = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1)
        for (i in actionData.indices){
            arrayAdapter.add(actionData[i].propName)
        }

        alertDialog.setAdapter(arrayAdapter) { dialog, which ->
            val view = inflater.inflate(actionData[which].layoutView, null, false)
            val btnDelete = view.findViewById<ImageView>(R.id.iv_delete)
            val action = actionData[which]

            if(action.propId == tag_action_output_port){
                val spinnerPort = view.findViewById<Spinner>(R.id.spinner_output_port)
                spinnerPort.adapter = arrayAdapterPort
            } else if(action.propId == tag_action_output_port_2){
                val spinnerPort = view.findViewById<Spinner>(R.id.spinner_output_port_2)
                spinnerPort.adapter = arrayAdapterPort
            }

            btnDelete.setOnClickListener {
                (view.parent as LinearLayout).removeView(view)
                selectedActionData.remove(action)
                actionData.add(action)
                arrayAdapter.add(action.propName)
                arrayAdapter.notifyDataSetChanged()
                if(arrayAdapter.count == 1){
                    mView.btn_add_action.visibility = View.VISIBLE
                }
            }

            selectedActionData.add(action)
            arrayAdapter.remove(action.propName)
            arrayAdapter.notifyDataSetChanged()
            actionData.remove(action)
            mView.ll_action.addView(view)
            dialog.dismiss()

            if(arrayAdapter.count == 0){
                mView.btn_add_action.visibility = View.GONE
            } else {
                mView.btn_add_action.visibility = View.VISIBLE
            }
        }

        mView.btn_add_action.setOnClickListener {
            alertDialog.show()
        }

        return mView
    }

    fun setData(){
        this.modeAdd = (activity as AddFlowActivity).getMode()

        (activity as AddFlowActivity).setDataStatus(false)
        mView.tv_device.text = newFlow!!.nodeId

        if(modeAdd){

        } else {
            mView.btn_add_action.visibility = View.GONE

            var nodeId = newFlow?.nodeId
            var actionControllerMaxLength: Int? = null
            var actionDrop = false
            var actionNormalMaxLength: Int? = null
            var actionFlood = false
            var actionFloodAll = false
            var outputPortData: MutableList<OutputPort> = ArrayList()

            if(newFlow?.instructions != null && newFlow?.instructions?.instruction!![0].applyActions != null){
                var mActionData = newFlow?.instructions?.instruction!![0].applyActions?.actionData
                for(i in mActionData?.indices!!){
                    if(mActionData[i].outputAction?.outputNodeConnector.equals("CONTROLLER")){
                        actionControllerMaxLength = mActionData[i].outputAction?.maxLength
                    } else if(mActionData[i].outputAction?.outputNodeConnector.equals("NORMAL")){
                        actionNormalMaxLength = mActionData[i].outputAction?.maxLength
                    } else if(mActionData[i].outputAction?.outputNodeConnector.equals("FLOOD")){
                        actionFlood = true
                    } else if(mActionData[i].outputAction?.outputNodeConnector.equals("FLOOD_ALL")){
                        actionFloodAll = true
                    } else if(mActionData[i].dropAction != null){
                        actionDrop = true
                    } else {
//                    var outputPort = inPort?.substring(0, inPort.lastIndexOf(":")+1) + actionData[i].outputAction?.outputNodeConnector
                        var outputPort = nodeId + ":" + mActionData[i].outputAction?.outputNodeConnector
                        var outputPortMaxLength = mActionData[i].outputAction?.maxLength

                        outputPortData.add(OutputPort(outputPort, outputPortMaxLength))
                    }
                }
            }
            if(actionDrop){
                try{
                    mView.tv_action_drop.visibility = View.VISIBLE
                } catch (e: Exception){
                    addView(0)

                    mView.tv_action_drop.visibility = View.VISIBLE
                }
            }

            if(actionFlood){
                try{
                    mView.tv_action_flood.visibility = View.VISIBLE
                } catch (e: Exception){
                    addView(1)

                    mView.tv_action_flood.visibility = View.VISIBLE
                }
            }

            if(actionFloodAll){
                try{
                    mView.tv_action_flood_all.visibility = View.VISIBLE
                } catch (e: Exception){
                    addView(2)

                    mView.tv_action_flood_all.visibility = View.VISIBLE
                }
            }

            if(actionControllerMaxLength != null){
                try{
                    mView.et_controller_max_length.visibility = View.VISIBLE
                    mView.et_controller_max_length.setText(actionControllerMaxLength.toString())
                } catch (e: Exception){
                    addView(3)

                    mView.et_controller_max_length.visibility = View.VISIBLE
                    mView.et_controller_max_length.setText(actionControllerMaxLength.toString())
                }
            }

            if(actionNormalMaxLength != null){
                try{
                    mView.et_normal_max_length.visibility = View.VISIBLE
                    mView.et_normal_max_length.setText(actionNormalMaxLength.toString())
                } catch (e: Exception){
                    addView(4)

                    mView.et_normal_max_length.visibility = View.VISIBLE
                    mView.et_normal_max_length.setText(actionNormalMaxLength.toString())
                }
            }

            println("AddFlowMatch, outputPortData.size : ${outputPortData.size}")
            if(outputPortData.size != 0){
                try{
                    var outputPort1 = outputPortData[0].outputPort
                    mView.spinner_output_port.visibility = View.VISIBLE
                    var spinnerPos = arrayAdapterPort?.getPosition(outputPort1)
                    mView.spinner_output_port.setSelection(spinnerPos!!)
                    mView.et_output_port_maxlength.setText(outputPortData[0].outputMaxLength!!.toString())
                } catch (e: Exception){
                    addView(5)

                    var outputPort1 = outputPortData[0].outputPort
                    mView.spinner_output_port.visibility = View.VISIBLE
                    var spinnerPos = arrayAdapterPort?.getPosition(outputPort1)
                    mView.spinner_output_port.setSelection(spinnerPos!!)
                    mView.et_output_port_maxlength.setText(outputPortData[0].outputMaxLength!!.toString())
                }

                if(outputPortData.size > 1){
                    try{
                        var outputPort2 = outputPortData[1].outputPort
                        mView.spinner_output_port_2.visibility = View.VISIBLE
                        var spinnerPos = arrayAdapterPort?.getPosition(outputPort2)
                        mView.spinner_output_port_2.setSelection(spinnerPos!!)
                        mView.et_output_port_2_max_length.setText(outputPortData[1].outputMaxLength!!.toString())
                    } catch (e: Exception){
                        addView(6)

                        var outputPort2 = outputPortData[1].outputPort
                        mView.spinner_output_port_2.visibility = View.VISIBLE
                        var spinnerPos = arrayAdapterPort?.getPosition(outputPort2)
                        mView.spinner_output_port_2.setSelection(spinnerPos!!)
                        mView.et_output_port_2_max_length.setText(outputPortData[1].outputMaxLength!!.toString())
                    }
                }
            }
        }
    }

    fun addView(which: Int){
        val view = inflater?.inflate(actionData[which].layoutView, null, false)
        val btnDelete = view?.findViewById<ImageView>(R.id.iv_delete)
        val action = actionData[which]

        btnDelete?.visibility = View.GONE

        if(action.propId == tag_action_output_port){
            val spinnerPort = view?.findViewById<Spinner>(R.id.spinner_output_port)
            spinnerPort?.adapter = arrayAdapterPort
        } else if(action.propId == tag_action_output_port_2){
            val spinnerPort = view?.findViewById<Spinner>(R.id.spinner_output_port_2)
            spinnerPort?.adapter = arrayAdapterPort
        }

        selectedActionData.add(action)

        mView.ll_action.addView(view)
    }

    fun setDefaultData(){
        actionData.clear()
        selectedActionData.clear()

        var drop = FlowProperties(tag_action_drop, "Drop",
                R.layout.layout_action_drop)
        actionData.add(drop)

        var flood = FlowProperties(tag_action_flood, "Flood",
                R.layout.layout_action_flood)
        actionData.add(flood)

        var floodAll = FlowProperties(tag_action_flood_all, "Flood All",
                R.layout.layout_action_flood_all)
        actionData.add(floodAll)

        var controller = FlowProperties(tag_action_controller, "Controller",
                R.layout.layout_action_controller)
        actionData.add(controller)

        var normal = FlowProperties(tag_action_normal, "Normal",
                R.layout.layout_action_normal)
        actionData.add(normal)

        var outputPort = FlowProperties(tag_action_output_port, "Output port",
                R.layout.layout_action_output_port)
        actionData.add(outputPort)

        var outputPort2 = FlowProperties(tag_action_output_port_2, "Output port 2",
                R.layout.layout_action_output_port_2)
        actionData.add(outputPort2)
    }

    fun setFlow(): Flow {
        var isCompleted = true
        println("setFlow ::::")
        newFlow = (activity as AddFlowActivity).getFlow()

        var actionData: MutableList<Action> = ArrayList()

        for (i in selectedActionData.indices){
            when(selectedActionData[i].propId){
                tag_action_drop -> {
                    println("setFlow :::: setDrop pos : $i")
                    var action = Action(i, null, DropAction())
                    actionData.add(action)
                }
                tag_action_flood -> {
                    var outputAction = OutputAction(0, "FLOOD")
                    val action = Action(i, outputAction, null)
                    actionData.add(action)
                }
                tag_action_flood_all -> {
                    var outputAction = OutputAction(0, "FLOOD_ALL")
                    val action = Action(i, outputAction, null)
                    actionData.add(action)
                }
                tag_action_controller -> {
                    if(!mView.et_controller_max_length.text.isNullOrEmpty() || !mView.et_controller_max_length.text.isNullOrEmpty()){
                        var maxLength = mView.et_controller_max_length.text.toString().toInt()
                        if(maxLength in 0..VALUE_MAX){
                            mView.et_controller_max_length.error = null
                            var outputAction = OutputAction(maxLength, "CONTROLLER")
                            val action = Action(i, outputAction, null)
                            actionData.add(action)
                        } else {
                            mView.et_controller_max_length.error = VALUE_ERROR
                            isCompleted = false
                        }
                    } else {
                        mView.et_controller_max_length.error = VALUE_CANT_BE_BLANK
                        isCompleted = false
                    }

                }
                tag_action_normal -> {
                    if(!mView.et_normal_max_length.text.isNullOrEmpty() || !mView.et_normal_max_length.text.isNullOrBlank()){
                        var maxLength = mView.et_normal_max_length.text.toString().toInt()
                        if(maxLength in 0..VALUE_MAX){
                            mView.et_normal_max_length.error = null
                            var outputAction = OutputAction(maxLength, "NORMAL")
                            val action = Action(i, outputAction, null)
                            actionData.add(action)
                        } else {
                            mView.et_normal_max_length.error = VALUE_ERROR
                            isCompleted = false
                        }
                    } else {
                        mView.et_normal_max_length.error = VALUE_CANT_BE_BLANK
                        isCompleted = false
                    }
                }
                tag_action_output_port -> {
                    if(!mView.et_output_port_maxlength.text.isNullOrEmpty() || !mView.et_output_port_maxlength.text.isNullOrBlank()){
                        var maxLength = mView.et_output_port_maxlength.text.toString().toInt()
                        if(maxLength in 0..VALUE_MAX){
                            mView.et_output_port_maxlength.error = null
                            var node = mView.spinner_output_port.selectedItem.toString()
                            var nodeConnector = node.substring(node.lastIndexOf(":")+1, node.length)
                            var outputAction = OutputAction(maxLength, nodeConnector)
                            val action = Action(i, outputAction, null)
                            actionData.add(action)
                        } else {
                            mView.et_output_port_maxlength.error = VALUE_ERROR
                            isCompleted = false
                        }
                    } else {
                        mView.et_output_port_maxlength.error = VALUE_CANT_BE_BLANK
                        isCompleted = false
                    }
                }
                tag_action_output_port_2 -> {
                    if(!mView.et_output_port_2_max_length.text.isNullOrEmpty() || !mView.et_output_port_2_max_length.text.isNullOrBlank()){
                        var maxLength = mView.et_output_port_2_max_length.text.toString().toInt()
                        if(maxLength in 0..VALUE_MAX){
                            mView.et_output_port_2_max_length.error = null
                            var node = mView.spinner_output_port_2.selectedItem.toString()
                            var nodeConnector = node.substring(node.lastIndexOf(":")+1, node.length)
                            var outputAction = OutputAction(maxLength, nodeConnector)
                            val action = Action(i, outputAction, null)
                            actionData.add(action)
                        } else {
                            mView.et_output_port_2_max_length.error = VALUE_ERROR
                            isCompleted = false
                        }
                    } else {
                        mView.et_output_port_2_max_length.error = VALUE_CANT_BE_BLANK
                        isCompleted = false
                    }
                }
            }
        }

        if(actionData.size != 0){
            var applyAction = ApplyActions(actionData)
            var instruction = Instruction(0, applyAction)

            var instructionData: MutableList<Instruction> = ArrayList()
            instructionData.add(instruction)

            var instructions = InstructionData(instructionData)

            newFlow?.instructions = instructions
        }

        if(isCompleted){
            (activity as AddFlowActivity).setDataStatus(true)
        } else {
            (activity as AddFlowActivity).setDataStatus(false)
        }

        return newFlow!!
    }

    data class OutputPort(
            var outputPort: String?,
            var outputMaxLength: Int?
    )
}