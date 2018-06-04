package com.edityomurti.openflowmanagerapp.ui.topology


import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.*

import com.edityomurti.openflowmanagerapp.R
import com.edityomurti.openflowmanagerapp.models.topology.Device
import com.edityomurti.openflowmanagerapp.models.topology.Link
import com.edityomurti.openflowmanagerapp.models.topology.NetworkTopology
import com.edityomurti.openflowmanagerapp.utils.Constants
import com.edityomurti.openflowmanagerapp.utils.RestAdapter
import kotlinx.android.synthetic.main.fragment_device_list.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 *
 */
class DeviceListFragment : Fragment() {
    private val TAG = "DeviceListFragment"
    lateinit var mView: View
    lateinit var restAdapter: RestAdapter
    lateinit var deviceListAdapter: DeviceListAdapter
    lateinit var layoutManager: LinearLayoutManager
    var deviceDataList: MutableList<Device> = ArrayList()
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_device_list, container, false)
        activity?.title = "Devices"

        sharedPreferences = activity?.getSharedPreferences(Constants.DEFAULT_PREFS_NAME, Activity.MODE_PRIVATE)!!
        deviceListAdapter = DeviceListAdapter(deviceDataList)
        layoutManager = LinearLayoutManager(context)

        setupRV()

        getNetworkTopology()

        return mView
    }

    fun setupRV(){
        mView.rv_device_list.setHasFixedSize(true)
        mView.rv_device_list.layoutManager = layoutManager
        mView.rv_device_list.adapter = deviceListAdapter
    }

    fun getNetworkTopology(){
        restAdapter = RestAdapter(context!!)
        showLoading(true)
        showRV(false)
        showNoConnection(false)
        restAdapter.getEndPoint().getNetworkTopology().enqueue(object : Callback<NetworkTopology>{
            override fun onResponse(call: Call<NetworkTopology>?, response: Response<NetworkTopology>?) {
                showLoading(false)
                if(response?.isSuccessful!!){
                    showNoConnection(false)
                    showRV(true)
                    var deviceData: MutableList<Device> = ArrayList()
                    var nodesData = response.body()?.topologyData?.topology?.get(0)?.nodeData
                    var linkData = response.body()?.topologyData?.topology?.get(0)?.linkData

                    if(nodesData?.size != 0 && nodesData?.size != null){
                        showNoData(false)
                        for(i in nodesData?.indices!!){
                            var deviceType = if(nodesData[i].hostTrackerId == null){
                                Constants.DEVICE_TYPE_SWITCH
                            } else {
                                Constants.DEVICE_TYPE_HOST
                            }
                            var deviceName = nodesData[i].nodeId
                            var deviceDesc = if(deviceType.equals(Constants.DEVICE_TYPE_SWITCH)){
                                "${nodesData[i].terminationPointData.size-1} nodes"
                            } else {
                                "${nodesData[i].hostTrackerAddressesData[0].ip}"
                            }
                            var linkDevice: MutableList<Link> = ArrayList()
                            if(linkData?.size != null){
                                for(j in linkData?.indices!!){
                                    if(linkData[j].source?.sourceNode?.equals(deviceName)!!){
                                        linkDevice.add(linkData[j])
                                    }
                                }
                            }
                            deviceData.add(Device(deviceName, deviceDesc, deviceType, linkDevice))
                            deviceData.sortWith(compareBy({it.deviceType}, {it.deviceName}))
                            deviceData.reverse()
                            deviceListAdapter.setData(deviceData)
                        }
                    } else {
                        showNoData(true)
                    }
                } else {
                    showNoConnection(true)
                    showRV(false)
                }
            }

            override fun onFailure(call: Call<NetworkTopology>?, t: Throwable?) {
                showLoading(false)
                println("$TAG , getNetworkTopology onFailure: ${t?.message}")
                showNoConnection(true)
                showRV(false)
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
            mView.rv_device_list.visibility = View.VISIBLE
        } else {
            mView.rv_device_list.visibility = View.GONE
        }
    }

    fun showNoData(isShowing: Boolean){
        if(isShowing){
            mView.ll_no_data.visibility = View.VISIBLE
        } else {
            mView.ll_no_data.visibility = View.GONE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_device_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.action_refresh -> getNetworkTopology()
        }
        return true
    }
}