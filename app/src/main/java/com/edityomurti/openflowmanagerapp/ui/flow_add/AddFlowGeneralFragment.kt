package com.edityomurti.openflowmanagerapp.ui.flow_add

import android.app.AlertDialog
import android.content.DialogInterface
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
import com.edityomurti.openflowmanagerapp.models.flowtable.flow.Flow
import kotlinx.android.synthetic.main.fragment_add_flow_general.*
import kotlinx.android.synthetic.main.fragment_add_flow_general.view.*
import kotlinx.android.synthetic.main.layout_prop_hard_timeout.view.*
import kotlinx.android.synthetic.main.layout_prop_idle_timeout.view.*

class AddFlowGeneralFragment : Fragment() {
    lateinit var mView: View

    var propData: MutableList<FlowProperties> = ArrayList()
    var selectedPropData: MutableList<FlowProperties> = ArrayList()

    val tag_prop_hardtimeout = "prop_hardtimeout"
    val tag_prop_idletimeout = "prop_idletimeout"
    val tag_prop_cookie = "prop_cookie"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_add_flow_general, container, false)

        activity?.title = "Add Flow: General Properties"

        setData()
        setDefaultData()

        var arrayAdapter = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1)
        var alertDialog = AlertDialog.Builder(context)
        for(i in propData.indices){
            arrayAdapter.add(propData[i].propName)
        }

        alertDialog.setAdapter(arrayAdapter) { dialog, which ->
            val view = inflater.inflate(propData[which].layoutView, null, false)
            val btnDelete = view.findViewById<ImageView>(R.id.iv_delete)
            val prop = propData[which]

            btnDelete.setOnClickListener {
                (view.parent as LinearLayout).removeView(view)
                selectedPropData.remove(prop)
                propData.add(prop)
                arrayAdapter.add(prop.propName)
                arrayAdapter.notifyDataSetChanged()
                if(arrayAdapter.count == 1){
                    mView.btn_add_properties.visibility = View.VISIBLE
                }
            }

            selectedPropData.add(prop)
            arrayAdapter.remove(prop.propName)
            arrayAdapter.notifyDataSetChanged()
            propData.remove(prop)

            mView.ll_properties.addView(view)
            dialog.dismiss()

            if(arrayAdapter.count == 0){
                mView.btn_add_properties.visibility = View.GONE
            } else {
                mView.btn_add_properties.visibility = View.VISIBLE
            }
        }

        mView.btn_add_properties.setOnClickListener {
            alertDialog.show()
        }

        return mView
    }

    fun setData(){
        (activity as AddFlowActivity).setDataStatus(false)
        mView.tv_device.text = (activity as AddFlowActivity).getFlow().nodeId
    }

    fun setDefaultData(){
        propData.clear()
        selectedPropData.clear()

        var hardTimeoutProp = FlowProperties(tag_prop_hardtimeout, "Hard timeout", R.layout.layout_prop_hard_timeout)
        propData.add(hardTimeoutProp)

        var idleTimeoutProp = FlowProperties(tag_prop_idletimeout, "Idle timeout", R.layout.layout_prop_idle_timeout)
        propData.add(idleTimeoutProp)

        var cookieProp = FlowProperties(tag_prop_cookie, "Cookie", R.layout.layout_prop_cookie)
        propData.add(cookieProp)
    }

    fun setFlow(): Flow {
        val newFlow = (activity as AddFlowActivity).getFlow()

        newFlow.tableId = 0
        newFlow.id = et_id_flow.text.toString()
        newFlow.priority = et_priority.text.toString().toInt()

        for(i in selectedPropData.indices){
            when(selectedPropData[i].propId){
                tag_prop_hardtimeout -> {
                    newFlow.hardTimeOut = mView.et_hard_timeout.text.toString().toInt()
                }
                tag_prop_idletimeout -> {
                    newFlow.idleTimeOut = mView.et_idle_timeout.text.toString().toInt()
                }
                tag_prop_cookie -> {
                    newFlow.cookie = mView.et_hard_timeout.text.toString().toBigInteger()
                }
            }
        }

        (activity as AddFlowActivity).setDataStatus(true)

        return newFlow
    }
}