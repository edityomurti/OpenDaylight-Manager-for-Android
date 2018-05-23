package com.edityomurti.openflowmanagerapp.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.view.MenuItem
import com.edityomurti.openflowmanagerapp.R
import com.edityomurti.openflowmanagerapp.ui.flowlist.FlowListFragment
import com.edityomurti.openflowmanagerapp.ui.settings.SettingsFragment
import com.edityomurti.openflowmanagerapp.ui.topology.DeviceListFragment
import com.edityomurti.openflowmanagerapp.utils.Constants
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    val TAG = "HomeActivity"

    lateinit var deviceListFragment: DeviceListFragment
    lateinit var flowListFragment: FlowListFragment
    lateinit var settingsFragment: SettingsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(ContextCompat.getColor(this@HomeActivity, R.color.white))
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
        }

        selectDeviceListFragment()

        drawer_layout.setStatusBarBackgroundColor(ContextCompat.getColor(this@HomeActivity, R.color.colorPrimary))
        nav_view.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            drawer_layout.closeDrawers()

            when (menuItem.itemId) {
                R.id.nav_devices -> selectDeviceListFragment()
                R.id.nav_flow -> selectFlowListFragment()
                R.id.nav_settings -> selectSettingsFragment()
            }
            true
        }

        nav_view.setCheckedItem(0)
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
