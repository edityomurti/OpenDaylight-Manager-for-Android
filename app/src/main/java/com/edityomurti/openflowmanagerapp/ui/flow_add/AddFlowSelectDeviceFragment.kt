package com.edityomurti.openflowmanagerapp.ui.flow_add


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter

import com.edityomurti.openflowmanagerapp.R
import com.edityomurti.openflowmanagerapp.models.flowtable.flow.Flow
import com.edityomurti.openflowmanagerapp.utils.Constants
import kotlinx.android.synthetic.main.fragment_add_flow_select_device.*
import kotlinx.android.synthetic.main.fragment_add_flow_select_device.view.*

class AddFlowSelectDeviceFragment : Fragment() {

    lateinit var mView: View

    companion object {
        fun newInstance(nodeList: ArrayList<String>): AddFlowSelectDeviceFragment{
            val args = Bundle()
            args.putSerializable(Constants.NODE_LIST, nodeList)
            var fragment = AddFlowSelectDeviceFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_add_flow_select_device, container, false)

        activity?.title = "Add Flow: Select Device"

        if(arguments != null){
            val nodeList = arguments!!.getSerializable(Constants.NODE_LIST) as ArrayList<String>
            val adapter = ArrayAdapter<String>(context, R.layout.item_spinner, nodeList)
            mView.spinner_select_device.adapter = adapter
        }

        return mView
    }

    fun setFlow(): Flow{
        var name = mView.spinner_select_device.selectedItem.toString()
        var newFlow = (activity as AddFlowActivity).getFlow()
        newFlow.nodeId = name
        (activity as AddFlowActivity).setDataStatus(true)
        return newFlow
    }
}
