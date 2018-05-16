package com.edityomurti.openflowmanagerapp.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.edityomurti.openflowmanagerapp.R
import com.edityomurti.openflowmanagerapp.ui.flowlist.FlowListFragment

class HomeActivity : AppCompatActivity() {
    val TAG = "HomeActivity"

    lateinit var flowListFragment: FlowListFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        selectFlowListFragment()
    }


    fun selectFlowListFragment(){
        flowListFragment = FlowListFragment()

        var fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, flowListFragment).commitAllowingStateLoss()
    }


}
