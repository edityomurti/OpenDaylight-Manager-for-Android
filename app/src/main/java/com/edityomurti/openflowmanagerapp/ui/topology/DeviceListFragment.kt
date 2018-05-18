package com.edityomurti.openflowmanagerapp.ui.topology


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.edityomurti.openflowmanagerapp.R

/**
 * A simple [Fragment] subclass.
 *
 */
class DeviceListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        activity?.title = "Devices"
        return inflater.inflate(R.layout.fragment_device_list, container, false)
    }
}
