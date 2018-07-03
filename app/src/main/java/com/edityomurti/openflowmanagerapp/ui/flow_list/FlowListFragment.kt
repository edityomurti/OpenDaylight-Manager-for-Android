package com.edityomurti.openflowmanagerapp.ui.flow_list

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.Toast
import com.edityomurti.openflowmanagerapp.R
import com.edityomurti.openflowmanagerapp.models.flowtable.FlowTableData
import com.edityomurti.openflowmanagerapp.models.flowtable.flow.Flow
import com.edityomurti.openflowmanagerapp.models.topology.Node
import com.edityomurti.openflowmanagerapp.models.topology.NodeDataSerializable
import com.edityomurti.openflowmanagerapp.models.topology.NodeSerializable
import com.edityomurti.openflowmanagerapp.models.topology.Nodes
import com.edityomurti.openflowmanagerapp.ui.flow_add.AddFlowActivity
import com.edityomurti.openflowmanagerapp.utils.Constants
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
    private var nodeList: ArrayList<String> = ArrayList()

    lateinit var sharedPreferences: SharedPreferences

    var nodeData: NodeDataSerializable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_flow_list, container, false)
        activity?.title = "Flow Table"

        sharedPreferences = activity?.getSharedPreferences(Constants.DEFAULT_PREFS_NAME, Activity.MODE_PRIVATE)!!
        flowListAdapter = FlowListAdapter(context!!, dataList, nodeList, nodeData)
        layoutManager = LinearLayoutManager(context)

        setupRV()

        getInventoryNodes()

        return mView
    }

    fun setupRV(){
        mView.rv_list_flow.setHasFixedSize(true)
        mView.rv_list_flow.layoutManager = layoutManager
        mView.rv_list_flow.adapter = flowListAdapter
    }

    fun getInventoryNodes(){
        restAdapter = RestAdapter(context!!)
        showLoading(true)
        showRV(false)
        showNoConnection(false)
        flowListAdapter.clearData()
        nodeList.clear()
        restAdapter.getEndPoint().getInventoryNodes().enqueue(object : Callback<Nodes> {
            override fun onResponse(call: Call<Nodes>?, response: Response<Nodes>?) {
                showLoading(false)
                if(response?.isSuccessful!!){
                    if(response.body() != null && response.body()?.nodeData?.node?.size != 0 && response.body()?.nodeData?.node?.size != null){
                        getFlowsId(response.body()?.nodeData?.node!!)
                        showNoData(false)
                    } else {
                        showNoData(true)
                    }
                    showNoConnection(false)
                    showRV(true)
                } else {
                    showNoConnection(true)
                    showRV(false)
                }
            }

            override fun onFailure(call: Call<Nodes>?, t: Throwable?) {
                showLoading(false)
                showNoConnection(true)
                showRV(false)
            }
        })
    }

    fun getFlowsId(data: MutableList<Node>){
        var nodeSerializableData: MutableList<NodeSerializable> = ArrayList()
        for(i in data.indices){
            if(data.get(i).ipAddress != null){
                var nodeId = data.get(i).id!!

                getFlows(data.get(i).id!!)
                nodeList.add(nodeId)

                var nodeConnector: ArrayList<String>? = ArrayList()

                for(j in data.get(i).nodeConnector!!.indices){
                    nodeConnector?.add(data.get(i).nodeConnector?.get(j)?.id!!)
                }

                var nodeSerializable = NodeSerializable(nodeId, nodeConnector!!)
                nodeSerializableData.add(nodeSerializable)
            }
        }
        nodeData = NodeDataSerializable(nodeSerializableData)
    }

    fun getFlows(nodeId: String){
        val tableId = "0"

        restAdapter.getEndPoint().getFlowsOperational(nodeId, tableId).enqueue(object : retrofit2.Callback<FlowTableData>{
            override fun onResponse(call: Call<FlowTableData>?, response: Response<FlowTableData>?) {
                showLoading(false)
                if(response?.isSuccessful!!){
                    if(response.body()?.table?.size != 0){
                        if (response.body()?.table?.get(0) != null && response.body()?.table?.get(0)?.flowData?.size != 0 && response.body()?.table?.get(0)?.flowData?.size != null){
                            var flowDataList = response.body()?.table?.get(0)?.flowData
                            for (i in flowDataList?.indices!!){
                                flowDataList[i].flowType = "operational"
                                flowDataList[i].nodeId = nodeId
                            }

                            flowListAdapter.addData(flowDataList, nodeList, nodeData)
                        } else {
                        }
                    } else {
                    }
                    showRV(true)
                    showNoConnection(false)
                } else {
                    println("$TAG , getFlows isNOTSuccessful")
                    showRV(false)
                    showNoConnection(true)
                }
            }

            override fun onFailure(call: Call<FlowTableData>?, t: Throwable?) {
                showLoading(false)
                println("$TAG , getFlows onFailure: ${t?.message}")
                showNoConnection(true)
            }
        })

        restAdapter.getEndPoint().getFlowsConfig(nodeId, tableId).enqueue(object : retrofit2.Callback<FlowTableData>{
            override fun onResponse(call: Call<FlowTableData>?, response: Response<FlowTableData>?) {
                println("$TAG , getflowsconfig onResponse nodeId : $nodeId")
                if(response?.isSuccessful!!){
                    if(response.body()?.table?.size != 0){
                        if (response.body()?.table?.get(0) != null && response.body()?.table?.get(0)?.flowData?.size != 0 && response.body()?.table?.get(0)?.flowData?.size != null){
                            var flowDataList = response.body()?.table?.get(0)?.flowData
                            for (i in flowDataList?.indices!!){
                                flowDataList[i].flowType = "config"
                                flowDataList[i].nodeId = nodeId
                            }

                            flowListAdapter.addData(flowDataList, nodeList, nodeData)
                            println("getflowsconfig, ADDING  FLOW")
                        } else {
                            println("getflowsconfig, NO DATA NULL FLOW")
                        }
                    } else {
                        println("getflowsconfig, NO DATA NULL DATA")
                    }
                } else {
                    println("$TAG , getflowsconfig isNOTSuccessful")
                    println("$TAG , getflowsconfig nodeId : $nodeId")
                    println("$TAG , getflowsconfig  response code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<FlowTableData>?, t: Throwable?) {
                println("$TAG , getflowsconfig onFailure nodeId : $nodeId ")
                println("$TAG , getflowsconfig onFailure message : ${t?.message} ")
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
    fun showNoConnection(isNoConnection: Boolean){
        if(isNoConnection){
            mView.ll_no_connection.visibility = View.VISIBLE
            mView.tv_ip_controller.text =
                    sharedPreferences.getString(Constants.CONTROLLER_IP_ADDRESS, "NO IP") +
                    ":" + sharedPreferences.getString(Constants.CONTROLLER_PORT_ADDRESS, "NO PORT")
        } else {
            mView.ll_no_connection.visibility = View.GONE
        }
    }
    fun showRV(isRVshowing: Boolean){
        if(isRVshowing){
            mView.rv_list_flow.visibility = View.VISIBLE
        } else {
            mView.rv_list_flow.visibility = View.GONE
        }
    }

    fun showNoData(isShowing: Boolean){
        if(isShowing){
            mView.ll_no_data.visibility = View.VISIBLE
        } else {
            mView.ll_no_data.visibility = View.GONE
        }
    }

    fun openAddFlowActivity(){
        if(nodeList.size > 0){
            var extras = Bundle()
            extras.putSerializable(Constants.NODE_DATA, nodeData)

            val intent = Intent(context, AddFlowActivity::class.java)
            intent.putStringArrayListExtra(Constants.NODE_LIST, nodeList)
            intent.putExtras(extras)
            intent.putExtra(Constants.ADD_MODE, Constants.MODE_ADD)
            startActivity(intent)
        } else {
            Toast.makeText(context, "No devices found!", Toast.LENGTH_SHORT).show()
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
            R.id.action_add -> openAddFlowActivity()
        }
        return super.onOptionsItemSelected(item)
    }
}