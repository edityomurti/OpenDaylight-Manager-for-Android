package com.edityomurti.openflowmanagerapp.ui.settings


import android.app.Activity
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast

import com.edityomurti.openflowmanagerapp.R
import com.edityomurti.openflowmanagerapp.utils.Constants
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_settings.view.*

/**
 * A simple [Fragment] subclass.
 *
 */
class SettingsFragment : Fragment() {

    lateinit var sharedPreferences: SharedPreferences

    lateinit var mView: View

    lateinit var ipAddress: String
    lateinit var portAddress: String
    lateinit var username: String
    lateinit var password: String
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_settings, container, false)

        activity?.title = "Settings"
        sharedPreferences = activity?.getSharedPreferences(Constants.DEFAULT_PREFS_NAME, Activity.MODE_PRIVATE)!!

        getData()

        mView.tv_save.setOnClickListener {
            setData()
        }

        return mView
    }

    fun getData(){
        if(!sharedPreferences.getString(Constants.CONTROLLER_IP_ADDRESS, null).isNullOrEmpty()){
            ipAddress = sharedPreferences.getString(Constants.CONTROLLER_IP_ADDRESS, null)
            mView.et_ip_address.text.clear()
            mView.et_ip_address.hint = ipAddress
        } else {
            mView.et_ip_address.hint = "e.g. 192.168.56.1"
        }

        if(!sharedPreferences.getString(Constants.CONTROLLER_PORT_ADDRESS, null).isNullOrEmpty()){
            portAddress = sharedPreferences.getString(Constants.CONTROLLER_PORT_ADDRESS, null)
            mView.et_port.text.clear()
            mView.et_port.hint = portAddress
        } else {
            mView.et_port.hint = "e.g. 8181"
        }

        if(!sharedPreferences.getString(Constants.CONTROLLER_USERNAME, null).isNullOrEmpty()){
            username = sharedPreferences.getString(Constants.CONTROLLER_USERNAME, null)
            mView.et_username.text.clear()
            mView.et_username.hint = username
        } else {
            mView.et_username.hint = "e.g. admin"
        }

        mView.et_password.setTypeface(Typeface.DEFAULT)
        mView.et_password.setTransformationMethod(PasswordTransformationMethod());
        if(!sharedPreferences.getString(Constants.CONTROLLER_PASSWORD, null).isNullOrEmpty()){
            password = sharedPreferences.getString(Constants.CONTROLLER_PASSWORD, null)
            mView.et_password.text.clear()
            mView.et_password.hint = password
        } else {
            mView.et_password.hint = "admin"
        }
    }

    fun setData(){
        mView.et_ip_address.clearFocus()
        mView.et_port.clearFocus()
        mView.et_username.clearFocus()
        mView.et_password.clearFocus()
        ipAddress = et_ip_address.text.toString()
        portAddress = et_port.text.toString()
        username = et_username.text.toString()
        password = et_password.text.toString()

        sharedPreferences.edit()
                .putString(Constants.CONTROLLER_IP_ADDRESS, ipAddress)
                .putString(Constants.CONTROLLER_PORT_ADDRESS, portAddress)
                .putString(Constants.CONTROLLER_USERNAME, username)
                .putString(Constants.CONTROLLER_PASSWORD, password)
                .apply()

        getData()
        Toast.makeText(context, "Settings Saved!", Toast.LENGTH_SHORT).show()
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

}
