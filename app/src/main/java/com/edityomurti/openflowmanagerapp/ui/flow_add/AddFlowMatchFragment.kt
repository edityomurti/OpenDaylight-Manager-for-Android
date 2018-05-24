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
import kotlinx.android.synthetic.main.fragment_add_flow_match.view.*

class AddFlowMatchFragment : Fragment() {

    lateinit var mView: View

    var matchData: MutableList<FlowProperties> = ArrayList()
    var selectedMatchData: MutableList<FlowProperties> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_add_flow_match, container, false)

        activity?.title = "Add Flow: Add Match"

        setDefaultData()

        var alertDialog = AlertDialog.Builder(context)
        var arrayAdapter = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1)
        for(i in matchData.indices){
            arrayAdapter.add(matchData[i].propName)
        }

        alertDialog.setAdapter(arrayAdapter) { dialog, which ->
            val view = inflater.inflate(matchData[which].layoutView, null, false)
            val btnDelete = view.findViewById<ImageView>(R.id.iv_delete)
            val match = matchData[which]

            btnDelete.setOnClickListener {
                (view.parent as LinearLayout).removeView(view)
                selectedMatchData.remove(match)
                matchData.add(match)
                arrayAdapter.add(match.propName)
                arrayAdapter.notifyDataSetChanged()
                if(arrayAdapter.count == 1){
                    mView.btn_add_match.visibility = View.VISIBLE
                }
            }

            selectedMatchData.add(match)
            arrayAdapter.remove(match.propName)
            arrayAdapter.notifyDataSetChanged()
            matchData.remove(match)

            mView.ll_match.addView(view)
            dialog.dismiss()

            if(arrayAdapter.count == 0){
                mView.btn_add_match.visibility = View.GONE
            } else {
                mView.btn_add_match.visibility = View.VISIBLE
            }
        }

        mView.btn_add_match.setOnClickListener {
            alertDialog.show()
        }

        return mView
    }

    fun setDefaultData(){
        matchData.clear()
        selectedMatchData.clear()

        var inPortMatch = FlowProperties("match_inport", "In port", R.layout.layout_match_in_port)
        matchData.add(inPortMatch)

        var ethernetType = FlowProperties("match_ehternettype", "Ethernet type", R.layout.layout_match_ethernet_type)
        matchData.add(ethernetType)

        var macSource = FlowProperties("match_macsource", "Source MAC", R.layout.layout_match_mac_source)
        matchData.add(macSource)

        var macDest = FlowProperties("match_macdest", "Destination MAC", R.layout.layout_match_mac_dest)
        matchData.add(macDest)

        var ipv4Source = FlowProperties("match_ipv4source", "IPv4 Source", R.layout.layout_match_ipv4_source)
        matchData.add(ipv4Source)

        var ipv4Dest = FlowProperties("match_ipv4dest", "IPv4 Destination", R.layout.layout_match_ipv4_dest)
        matchData.add(ipv4Dest)

    }

}
