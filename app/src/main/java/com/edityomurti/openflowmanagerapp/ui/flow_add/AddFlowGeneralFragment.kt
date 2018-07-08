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
import kotlinx.android.synthetic.main.layout_prop_cookie.view.*
import kotlinx.android.synthetic.main.layout_prop_hard_timeout.view.*
import kotlinx.android.synthetic.main.layout_prop_idle_timeout.view.*

class AddFlowGeneralFragment : Fragment() {
    lateinit var mView: View

    var propData: MutableList<FlowProperties> = ArrayList()

    var selectedPropData: MutableList<FlowProperties> = ArrayList()

    val tag_prop_hardtimeout = "prop_hardtimeout"
    val tag_prop_idletimeout = "prop_idletimeout"
    val tag_prop_cookie = "prop_cookie"

    val VALUE_MAX = 65535
    val VALUE_CANT_BE_BLANK = "Cannot be blank"
    val VALUE_ERROR = "Value must between 0 - $VALUE_MAX"

    var newFlow: Flow? = null

    var modeAdd = true

    var inflater: LayoutInflater? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_add_flow_general, container, false)
        this.inflater = inflater

        setDefaultData()
        setData()

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
        this.modeAdd = (activity as AddFlowActivity).getMode()

        newFlow = (activity as AddFlowActivity).getFlow()

        (activity as AddFlowActivity).setDataStatus(false)
        mView.tv_device.text = newFlow?.nodeId

        if(modeAdd){
            activity?.title = "Add Flow: General Properties"
        } else {
            activity?.title = "Edit Flow: General Properties"
            mView.btn_add_properties.visibility = View.GONE

            val flowId = newFlow?.id
            val priority = newFlow?.priority
            val hardTimeOut = newFlow?.hardTimeOut
            val idleTimeOut = newFlow?.idleTimeOut
            val cookie = newFlow?.cookie

//             IF ELSE KONDISI PENAMPILAN DATA
            if(flowId != null){
                mView.et_id_flow.setText(flowId)
                mView.et_id_flow.isEnabled = false
            }

            if(priority != null){
                mView.et_priority.setText(priority.toString())
                mView.et_priority.isEnabled = false
            }

            if(hardTimeOut != null){
                println("AddView, adding 0")
                try {
                    mView.et_hard_timeout.visibility = View.VISIBLE
                    mView.et_hard_timeout.setText(hardTimeOut.toString())
                } catch (e: Exception){
                    addView(0)

                    mView.et_hard_timeout.visibility = View.VISIBLE
                    mView.et_hard_timeout.setText(hardTimeOut.toString())
                }
            }

            if(idleTimeOut != null){
                try{
                    mView.et_idle_timeout.visibility = View.VISIBLE
                    mView.et_idle_timeout.setText(idleTimeOut.toString())
                } catch (e: Exception){
                    println("AddView, adding 1")
                    addView(1)

                    mView.et_idle_timeout.visibility = View.VISIBLE
                    mView.et_idle_timeout.setText(idleTimeOut.toString())
                }
            }

            if(cookie != null){
                try {
                    mView.et_cookie.visibility = View.VISIBLE
                    mView.et_cookie.setText(cookie.toString())
                } catch (e: Exception){
                    println("AddView, adding 2")
                    addView(2)

                    mView.et_cookie.visibility = View.VISIBLE
                    mView.et_cookie.setText(cookie.toString())
                }
            }
        }
    }

    fun addView(which: Int){
        val view = inflater?.inflate(propData[which].layoutView, null, false)
        val btnDelete = view?.findViewById<ImageView>(R.id.iv_delete)
        val prop = propData[which]

//        btnDelete?.setOnClickListener {
//            (view.parent as LinearLayout).removeView(view)
//            selectedPropData.remove(prop)
//            propData.add(prop)
////                    arrayAdapter.add(prop.propName)
////                    arrayAdapter.notifyDataSetChanged()
////                    if(arrayAdapter.count == 1){
////                        mView.btn_add_properties.visibility = View.VISIBLE
////                    }
//        }

        btnDelete?.visibility = View.GONE

        selectedPropData.add(prop)
//                arrayAdapter.remove(prop.propName)
//                arrayAdapter.notifyDataSetChanged()
//        propData.remove(prop)

        mView.ll_properties.addView(view)
//                if(arrayAdapter.count == 0){
//                    mView.btn_add_properties.visibility = View.GONE
//                } else {
//                    mView.btn_add_properties.visibility = View.VISIBLE
//                }
    }

    fun setDefaultData(){
        propData.clear()
        selectedPropData.clear()

        newFlow = (activity as AddFlowActivity).getFlow()

        var hardTimeoutProp = FlowProperties(tag_prop_hardtimeout, "Hard timeout", R.layout.layout_prop_hard_timeout)
        propData.add(hardTimeoutProp)

        var idleTimeoutProp = FlowProperties(tag_prop_idletimeout, "Idle timeout", R.layout.layout_prop_idle_timeout)
        propData.add(idleTimeoutProp)

        var cookieProp = FlowProperties(tag_prop_cookie, "Cookie", R.layout.layout_prop_cookie)
        propData.add(cookieProp)
    }

    fun setFlow(): Flow {
        var isCompleted = true

        newFlow = (activity as AddFlowActivity).getFlow()

        newFlow?.tableId = 0

        if (!mView.et_id_flow.text.isNullOrBlank() && !mView.et_id_flow.text.isNullOrEmpty()){
            val id = mView.et_id_flow.text.toString()
            mView.et_id_flow.error = null
            newFlow?.id = id
        } else {
            mView.et_id_flow.error = VALUE_CANT_BE_BLANK
            isCompleted = false
        }

        if(!mView.et_priority.text.isNullOrEmpty() && !mView.et_priority.text.isNullOrBlank()){
            try{
                val priority = mView.et_priority.text.toString().toInt()
                if(priority in 0..VALUE_MAX){
                    mView.et_priority.error = null
                    newFlow?.priority = priority
                } else {
                    mView.et_priority.error = VALUE_ERROR
                    isCompleted = false
                }
            } catch (e: Exception){
                mView.et_priority.error = VALUE_ERROR
                isCompleted = false
            }
        } else {
            mView.et_priority.error = VALUE_CANT_BE_BLANK
            isCompleted = false
        }

        for(i in selectedPropData.indices){
            when(selectedPropData[i].propId){
                tag_prop_hardtimeout -> {
                    if(!mView.et_hard_timeout.text.isNullOrEmpty() && !mView.et_hard_timeout.text.isNullOrBlank()){
                        try{
                            val hardTimeout = mView.et_hard_timeout.text.toString().toInt()
                            if(hardTimeout in 0..VALUE_MAX){
                                mView.et_hard_timeout.error = null
                                newFlow?.hardTimeOut = hardTimeout
                            } else {
                                mView.et_hard_timeout.error = VALUE_ERROR
                                isCompleted = false
                            }
                        } catch (e: Exception){
                            mView.et_hard_timeout.error = VALUE_ERROR
                            isCompleted = false
                        }
                    } else {
                        mView.et_hard_timeout.error = VALUE_CANT_BE_BLANK
                        isCompleted = false
                    }
                }
                tag_prop_idletimeout -> {
                    if(!mView.et_idle_timeout.text.isNullOrEmpty() && !mView.et_idle_timeout.text.isNullOrBlank()){
                        try{
                            val idleTimeOut = mView.et_idle_timeout.text.toString().toInt()
                            if(idleTimeOut in 0..VALUE_MAX){
                                mView.et_idle_timeout.error = null
                                newFlow?.idleTimeOut = idleTimeOut
                            } else {
                                mView.et_idle_timeout.error = VALUE_ERROR
                                isCompleted = false
                            }
                        } catch (e: Exception){
                            mView.et_idle_timeout.error = VALUE_ERROR
                            isCompleted = false
                        }
                    } else {
                        mView.et_idle_timeout.error = VALUE_CANT_BE_BLANK
                        isCompleted = false
                    }
                }
                tag_prop_cookie -> {
                    if(!mView.et_cookie.text.isNullOrEmpty() && !mView.et_cookie.text.isNullOrBlank()){
                        val cookie = mView.et_cookie.text.toString().toBigInteger()
                        mView.et_cookie.error = null
                        newFlow?.cookie = cookie
                    } else {
                        mView.et_cookie.error = VALUE_CANT_BE_BLANK
                        isCompleted = false
                    }
                }
            }
        }

        if(isCompleted){
            (activity as AddFlowActivity).setDataStatus(true)
        } else {
            (activity as AddFlowActivity).setDataStatus(false)
        }

        return newFlow!!
    }
}