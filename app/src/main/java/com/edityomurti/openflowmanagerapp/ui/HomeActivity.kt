package com.edityomurti.openflowmanagerapp.ui

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.edityomurti.openflowmanagerapp.R
import com.edityomurti.openflowmanagerapp.ui.flow_list.FlowListFragment
import com.edityomurti.openflowmanagerapp.ui.settings.SettingsFragment
import com.edityomurti.openflowmanagerapp.ui.topology.DeviceListFragment
import com.edityomurti.openflowmanagerapp.utils.Constants
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    val TAG = "HomeActivity"

    lateinit var deviceListFragment: DeviceListFragment
    lateinit var flowListFragment: FlowListFragment
    lateinit var settingsFragment: SettingsFragment

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        sharedPreferences = getSharedPreferences(Constants.DEFAULT_PREFS_NAME, Activity.MODE_PRIVATE)!!

        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(ContextCompat.getColor(this@HomeActivity, R.color.white))
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
        }

        selectDeviceListFragment()

        drawer_layout.setStatusBarBackgroundColor(ContextCompat.getColor(this@HomeActivity, R.color.colorPrimary))
        setNavigationHeaderData()

        nav_view.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            drawer_layout.closeDrawers()

            val focus = this.currentFocus
            if(focus != null){
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(focus.windowToken, 0)
            }

            when (menuItem.itemId) {
                R.id.nav_devices -> selectDeviceListFragment()
                R.id.nav_flow -> selectFlowListFragment()
                R.id.nav_settings -> selectSettingsFragment()
            }
            true
        }

        nav_view.setCheckedItem(0)
    }

    fun setNavigationHeaderData(){
        var username: String? = null
        var ipAddress: String? = null
        var portAddress: String? = null

        val navView = nav_view.getHeaderView(0)

        if(!sharedPreferences.getString(Constants.CONTROLLER_USERNAME, null).isNullOrEmpty()){
            username = sharedPreferences.getString(Constants.CONTROLLER_USERNAME, null)
            val tvUsername = navView.findViewById<TextView>(R.id.tv_username)
            tvUsername.text = username
        }

        if(!sharedPreferences.getString(Constants.CONTROLLER_IP_ADDRESS, null).isNullOrEmpty()){
            ipAddress = sharedPreferences.getString(Constants.CONTROLLER_IP_ADDRESS, null)
            val tvAddress = navView.findViewById<TextView>(R.id.tv_ip_controller)
            tvAddress.setText(ipAddress)
        }

        if(!sharedPreferences.getString(Constants.CONTROLLER_PORT_ADDRESS, null).isNullOrEmpty()){
            portAddress = ":" + sharedPreferences.getString(Constants.CONTROLLER_PORT_ADDRESS, null)
            val tvAddress = navView.findViewById<TextView>(R.id.tv_ip_controller)
            tvAddress.setText("" + tvAddress.getText() + portAddress)
        }
    }

    fun selectDeviceListFragment(){
        title = "Devices"
        deviceListFragment = DeviceListFragment()

        if(supportFragmentManager.findFragmentByTag(Constants.TAGS_DEVICE_LIST_FRAGMENT) != null){
            supportFragmentManager.beginTransaction().show(supportFragmentManager.findFragmentByTag(Constants.TAGS_DEVICE_LIST_FRAGMENT)).commitAllowingStateLoss()
        } else {
            supportFragmentManager.beginTransaction().add(R.id.fragment_container, deviceListFragment, Constants.TAGS_DEVICE_LIST_FRAGMENT).commitAllowingStateLoss()
        }
        if(supportFragmentManager.findFragmentByTag(Constants.TAGS_FLOW_LIST_FRAGMENT) != null){
            supportFragmentManager.beginTransaction().hide(supportFragmentManager.findFragmentByTag(Constants.TAGS_FLOW_LIST_FRAGMENT)).commitAllowingStateLoss()
        }
        if(supportFragmentManager.findFragmentByTag(Constants.TAGS_SETTINGS_FRAGMENT) != null){
            supportFragmentManager.beginTransaction().hide(supportFragmentManager.findFragmentByTag(Constants.TAGS_SETTINGS_FRAGMENT)).commitAllowingStateLoss()
        }
    }

    fun selectFlowListFragment(){
        title = "Flow Table"
        flowListFragment = FlowListFragment()

        if(supportFragmentManager.findFragmentByTag(Constants.TAGS_FLOW_LIST_FRAGMENT) != null){
            supportFragmentManager.beginTransaction().show(supportFragmentManager.findFragmentByTag(Constants.TAGS_FLOW_LIST_FRAGMENT)).commitAllowingStateLoss()
        } else {
            supportFragmentManager.beginTransaction().add(R.id.fragment_container, flowListFragment, Constants.TAGS_FLOW_LIST_FRAGMENT).commitAllowingStateLoss()
        }
        if(supportFragmentManager.findFragmentByTag(Constants.TAGS_DEVICE_LIST_FRAGMENT) != null){
            supportFragmentManager.beginTransaction().hide(supportFragmentManager.findFragmentByTag(Constants.TAGS_DEVICE_LIST_FRAGMENT)).commitAllowingStateLoss()
        }
        if(supportFragmentManager.findFragmentByTag(Constants.TAGS_SETTINGS_FRAGMENT) != null){
            supportFragmentManager.beginTransaction().hide(supportFragmentManager.findFragmentByTag(Constants.TAGS_SETTINGS_FRAGMENT)).commitAllowingStateLoss()
        }
    }

    fun selectSettingsFragment(){
        title = "Settings"
        settingsFragment = SettingsFragment()

        if(supportFragmentManager.findFragmentByTag(Constants.TAGS_SETTINGS_FRAGMENT) != null){
            supportFragmentManager.beginTransaction().show(supportFragmentManager.findFragmentByTag(Constants.TAGS_SETTINGS_FRAGMENT)).commitAllowingStateLoss()
        } else {
            supportFragmentManager.beginTransaction().add(R.id.fragment_container, settingsFragment, Constants.TAGS_SETTINGS_FRAGMENT).commitAllowingStateLoss()
        }
        if(supportFragmentManager.findFragmentByTag(Constants.TAGS_DEVICE_LIST_FRAGMENT) != null){
            supportFragmentManager.beginTransaction().hide(supportFragmentManager.findFragmentByTag(Constants.TAGS_DEVICE_LIST_FRAGMENT)).commitAllowingStateLoss()
        }
        if(supportFragmentManager.findFragmentByTag(Constants.TAGS_FLOW_LIST_FRAGMENT) != null){
            supportFragmentManager.beginTransaction().hide(supportFragmentManager.findFragmentByTag(Constants.TAGS_FLOW_LIST_FRAGMENT)).commitAllowingStateLoss()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId){
            android.R.id.home -> {
                drawer_layout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
