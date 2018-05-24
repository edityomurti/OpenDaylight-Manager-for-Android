package com.edityomurti.openflowmanagerapp.ui.flow_add

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.edityomurti.openflowmanagerapp.R
import com.edityomurti.openflowmanagerapp.utils.Constants
import kotlinx.android.synthetic.main.activity_add_flow.*

class AddFlowActivity : AppCompatActivity() {

    val TAG_SELECT_DEVICE_FRAGMENT = "TAG_SELECT_DEVICE_FRAGMENT"
    val TAG_GENERAL_PROPERTIES_FRAGMENT = "TAG_GENERAL_PROPERTIES"
    val TAG_MATCH_FRAGMENT = "TAG_MATCH_FRAGMENT"
    val TAG_ACTION_FRAGMENT = "TAG_ACTION_FRAGMENT"

    lateinit var selectDeviceFragment: AddFlowSelectDeviceFragment
    lateinit var generalPropertiesFragment: AddFlowGeneralFragment

    lateinit var nodeList: ArrayList<String>

    private var currentPosition = 1
    private val MAX_POSITION = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_flow)

        title = "Add Flow"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        nodeList = intent.getStringArrayListExtra(Constants.NODE_LIST)
        selectDeviceFragment = AddFlowSelectDeviceFragment.newInstance(nodeList)
        generalPropertiesFragment = AddFlowGeneralFragment()

        setNavigation()

        btn_next.setOnClickListener{
            if(currentPosition != MAX_POSITION){
                currentPosition += 1
                onClickNavigation()
            }
        }
        btn_previous.setOnClickListener{
                currentPosition -= 1
                onClickNavigation()
        }
    }

    fun setNavigation(){
        when(currentPosition){
            1 -> {
                btn_previous.visibility = View.INVISIBLE
                tv_next.visibility = View.VISIBLE
                showSelectDevice()
            }
            2 -> {
                btn_previous.visibility = View.VISIBLE
                btn_next.visibility = View.VISIBLE
                tv_next.text = "Next"
                showGeneralProp()
            }
            MAX_POSITION -> {
                btn_previous.visibility = View.VISIBLE
                btn_next.visibility = View.VISIBLE
                tv_next.text = "Finish"
            }
            else -> {
                btn_previous.visibility = View.VISIBLE
                btn_next.visibility = View.VISIBLE
                tv_next.text = "Next"
            }
        }
    }

    fun showSelectDevice(){
        var transaction = supportFragmentManager.beginTransaction()
//        transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)

        if(supportFragmentManager.findFragmentByTag(TAG_GENERAL_PROPERTIES_FRAGMENT) != null){
            transaction.hide(supportFragmentManager.findFragmentByTag(TAG_GENERAL_PROPERTIES_FRAGMENT))
        }

        if(supportFragmentManager.findFragmentByTag(TAG_SELECT_DEVICE_FRAGMENT) != null){
            transaction.show(supportFragmentManager.findFragmentByTag(TAG_SELECT_DEVICE_FRAGMENT))
        } else {
            transaction.add(R.id.fragment_container, selectDeviceFragment, TAG_SELECT_DEVICE_FRAGMENT)
        }
        transaction.commitAllowingStateLoss()
    }

    fun showGeneralProp(){
        var transaction = supportFragmentManager.beginTransaction()
//        transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
        if(supportFragmentManager.findFragmentByTag(TAG_SELECT_DEVICE_FRAGMENT) != null){
            transaction.hide(supportFragmentManager.findFragmentByTag(TAG_SELECT_DEVICE_FRAGMENT))
        }
        if(supportFragmentManager.findFragmentByTag(TAG_GENERAL_PROPERTIES_FRAGMENT) != null){
            transaction.show(supportFragmentManager.findFragmentByTag(TAG_GENERAL_PROPERTIES_FRAGMENT))
        }else {
            transaction.add(R.id.fragment_container, generalPropertiesFragment, TAG_GENERAL_PROPERTIES_FRAGMENT)
        }
        transaction.commitAllowingStateLoss()
    }

    fun onClickNavigation(){
        Toast.makeText(this, currentPosition.toString(), Toast.LENGTH_SHORT).show()
        setNavigation()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
