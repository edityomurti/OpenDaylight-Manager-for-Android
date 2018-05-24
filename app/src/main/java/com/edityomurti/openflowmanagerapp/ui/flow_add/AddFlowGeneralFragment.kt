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
import kotlinx.android.synthetic.main.fragment_add_flow_general.view.*

class AddFlowGeneralFragment : Fragment() {
    lateinit var mView: View

    var propData: MutableList<FlowProperties> = ArrayList()
    var selectedPropData: MutableList<FlowProperties> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_add_flow_general, container, false)

        activity?.title = "Add Flow: General Properties"

        setDefaultData()

        var alertDialog = AlertDialog.Builder(context)
        var arrayAdapter = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1)
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
                println("onDelete count : ${arrayAdapter.count}")
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

    fun setDefaultData(){
        propData.clear()
        selectedPropData.clear()

        var hardTimeoutProp = FlowProperties("prop_hardtimeout", "Hard timeout", R.layout.layout_prop_hard_timeout)
        propData.add(hardTimeoutProp)

        var idleTimeoutProp = FlowProperties("prop_idletimeout", "Idle timeout", R.layout.layout_prop_idle_timeout)
        propData.add(idleTimeoutProp)

        var cookieProp = FlowProperties("prop_cookie", "Cookie", R.layout.layout_prop_cookie)
        propData.add(cookieProp)
    }
}