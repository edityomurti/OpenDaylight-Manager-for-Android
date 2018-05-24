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

import com.edityomurti.openflowmanagerapp.R
import com.edityomurti.openflowmanagerapp.models.FlowProperties
import kotlinx.android.synthetic.main.fragment_add_flow_action.view.*

class AddFlowActionFragment : Fragment() {

    lateinit var mView: View

    var actionData: MutableList<FlowProperties> = ArrayList()
    var selectedActionData: MutableList<FlowProperties> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_add_flow_action, container, false)

        setDefaultData()

        var alertDialog = AlertDialog.Builder(context)
        var arrayAdapter = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1)
        for (i in actionData.indices){
            arrayAdapter.add(actionData[i].propName)
        }

        alertDialog.setAdapter(arrayAdapter) { dialog, which ->
            val view = inflater.inflate(actionData[which].layoutView, null, false)
            val btnDelete = view.findViewById<ImageView>(R.id.iv_delete)
            val action = actionData[which]

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

    fun setDefaultData(){
        actionData.clear()
        selectedActionData.clear()

        var drop = FlowProperties("action_drop", "Drop",
                R.layout.layout_action_drop)
        actionData.add(drop)

        var flood = FlowProperties("action_drop", "Flood",
                R.layout.layout_action_flood)
        actionData.add(flood)

        var floodAll = FlowProperties("action_flood", "Flood All",
                R.layout.layout_action_flood_all)
        actionData.add(floodAll)

        var controller = FlowProperties("action_controller", "Controller",
                R.layout.layout_action_controller)
        actionData.add(controller)

        var normal = FlowProperties("action_normal", "Normal",
                R.layout.layout_action_normal)
        actionData.add(normal)

        var outputPort = FlowProperties("action_output_port", "Output port",
                R.layout.layout_action_output_port)
        actionData.add(outputPort)

        var outputPort2 = FlowProperties("action_output_port_2", "Output port 2",
                R.layout.layout_action_output_port_2)
        actionData.add(outputPort2)

    }
}
