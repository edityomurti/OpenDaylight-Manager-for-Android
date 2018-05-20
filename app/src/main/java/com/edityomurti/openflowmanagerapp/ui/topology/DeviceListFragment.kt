package com.edityomurti.openflowmanagerapp.ui.topology


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.*

import com.edityomurti.openflowmanagerapp.R
import com.edityomurti.openflowmanagerapp.models.topology.NetworkTopology
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
    var networkTopology: NetworkTopology? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_device_list, container, false)
        activity?.title = "Devices"

        deviceListAdapter = DeviceListAdapter(networkTopology)
        layoutManager = LinearLayoutManager(context)

        setupRV()

        restAdapter = RestAdapter()
        getNetworkTopology()

        return mView
    }

    fun setupRV(){
        mView.rv_device_list.setHasFixedSize(true)
        mView.rv_device_list.layoutManager = layoutManager
        mView.rv_device_list.adapter = deviceListAdapter
    }

    fun getNetworkTopology(){
        restAdapter.getEndPoint().getNetworkTopology().enqueue(object : Callback<NetworkTopology>{
            override fun onResponse(call: Call<NetworkTopology>?, response: Response<NetworkTopology>?) {
                if(response?.isSuccessful!!){
                    deviceListAdapter.setData(response.body()!!)
                }
            }

            override fun onFailure(call: Call<NetworkTopology>?, t: Throwable?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
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