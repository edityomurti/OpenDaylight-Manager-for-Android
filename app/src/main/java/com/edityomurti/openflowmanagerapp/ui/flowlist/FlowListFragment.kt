package com.edityomurti.openflowmanagerapp.ui.flowlist

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.Toast
import com.edityomurti.openflowmanagerapp.R
import com.edityomurti.openflowmanagerapp.models.flowtable.FlowTableData
import com.edityomurti.openflowmanagerapp.models.flowtable.flow.Flow
import com.edityomurti.openflowmanagerapp.models.topology.Node
import com.edityomurti.openflowmanagerapp.models.topology.Nodes
import com.edityomurti.openflowmanagerapp.utils.RestAdapter
import kotlinx.android.synthetic.main.fragment_flow_list.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 *
 */
class FlowListFragment : Fragment() {
    private val TAG = "FlowListFragment"

    lateinit var mView: View

    lateinit var restAdapter: RestAdapter

    lateinit var flowListAdapter: FlowListAdapter
    lateinit var layoutManager: LinearLayoutManager

    private var dataList: MutableList<Flow> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_flow_list, container, false)
        activity?.title = "Flows"

        flowListAdapter = FlowListAdapter(dataList)
        layoutManager = LinearLayoutManager(context)

        setupRV()

        restAdapter = RestAdapter(context!!)
        getInventoryNodes()

        return mView
    }

    fun setupRV(){
        mView.rv_list_flow.setHasFixedSize(true)
        mView.rv_list_flow.layoutManager = layoutManager
        mView.rv_list_flow.adapter = flowListAdapter
    }

    fun getInventoryNodes(){
        showLoading(true)
        restAdapter.getEndPoint().getInventoryNodes().enqueue(object : Callback<Nodes> {
            override fun onResponse(call: Call<Nodes>?, response: Response<Nodes>?) {
                if(response?.isSuccessful!!){
                    if(response.body() != null || response.body()?.nodeData?.node?.size != 0){
                        getFlowsId(response.body()?.nodeData?.node!!)
                    }
                }
            }

            override fun onFailure(call: Call<Nodes>?, t: Throwable?) {
                println("onFailure getInventoryNodes ${t?.message}")
            }
        })
    }

    fun getFlowsId(data: MutableList<Node>){
        for(i in data.indices){
            if(data.get(i).ipAddress != null){
                println("getFlowsId, FOUND : ${data.get(i).id}")
                getFlows(data.get(i).id!!)
            }
        }
    }

    fun getFlows(nodeId: String){
        val tableId = "0"

        restAdapter.getEndPoint().getFlows(nodeId, tableId).enqueue(object : retrofit2.Callback<FlowTableData>{
            override fun onResponse(call: Call<FlowTableData>?, response: Response<FlowTableData>?) {
                showLoading(false)
                if(response?.isSuccessful!!){
                    if(response.body()?.table?.size != 0){
                        if (response.body()?.table?.get(0) != null || response.body()?.table?.get(0)?.flowData?.size != 0){
                            var flowDataList = response.body()?.table?.get(0)?.flowData
                            for (i in flowDataList?.indices!!){
                                flowDataList[i].nodeId = nodeId
                            }

                            flowListAdapter.addData(flowDataList)
//                            dataList.addAll(flowDataList)
//                            flowListAdapter.notifyDataSetChanged()
                            println("getFlows, response.body()?.table?.get(0) != null ||response.body()?.table?.get(0)?.flowData?.size != 0")
                        } else {
                            println("getFlows, response.body()?.table?.get(0) == null ||response.body()?.table?.get(0)?.flowData?.size == 0")
                        }
                    } else {
                        println("getFlows, response.body()?.table?.size -= 0")
                    }
                } else {
                    println("$TAG , getFlows isNOTSuccessful")
                }
            }

            override fun onFailure(call: Call<FlowTableData>?, t: Throwable?) {
                showLoading(false)
                println("$TAG , getFlows onFailure: ${t?.message}")
//                Toast.makeText(context, "Request data failed, Please check your settings ..", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun showLoading(isLoading: Boolean){
        if(isLoading){
            mView.progressbar.visibility = View.VISIBLE
        } else {
            mView.progressbar.visibility = View.GONE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_flow_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.action_refresh -> getInventoryNodes()
        }
        return super.onOptionsItemSelected(item)
    }
}
