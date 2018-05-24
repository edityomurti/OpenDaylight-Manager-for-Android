package com.edityomurti.openflowmanagerapp.ui.flow_add


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.edityomurti.openflowmanagerapp.R
import com.edityomurti.openflowmanagerapp.models.FlowProperties

class AddFlowMatchFragment : Fragment() {

    lateinit var mView: View

    var matchData: MutableList<FlowProperties> = ArrayList()
    var selectedMatchData: MutableList<FlowProperties> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_add_flow_match, container, false)

        activity?.title = "Add Flow: Add Match"

        setDefaultData()

        return mView
    }

    fun setDefaultData(){
        matchData.clear()
        selectedMatchData.clear()

        var inPortMatch = FlowProperties("match_inport", "In port", )
    }

}
