package com.edityomurti.openflowmanagerapp.ui.flow_add


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.edityomurti.openflowmanagerapp.R
import com.edityomurti.openflowmanagerapp.models.flowtable.flow.Flow
import com.edityomurti.openflowmanagerapp.models.flowtable.flow.FlowDataSent
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_add_flow_review.view.*

class AddFlowReviewFragment : Fragment() {

    lateinit var mView: View

    lateinit var jsonInString: String
    lateinit var newFlow: Flow

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_add_flow_review, container, false)
        newFlow = (activity as AddFlowActivity).getFlow()

        setData()

        return mView
    }

    fun setData(){
        val nodeId = newFlow.nodeId
        newFlow.nodeId = null

        var flowData: MutableList<Flow> = ArrayList()
        flowData.add(newFlow)
        val flowDataSent = FlowDataSent(flowData)

        jsonInString = Gson().toJson(flowDataSent)

        mView.tv_json.text = jsonInString
        mView.rv_json.bindJson(jsonInString)

        val px12 = Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 12f, resources.getDisplayMetrics()))

        mView.rv_json.setTextSize(px12)

        println("setFLow review in port data : ${newFlow?.match?.inPort}")

        newFlow.nodeId = nodeId
    }
}