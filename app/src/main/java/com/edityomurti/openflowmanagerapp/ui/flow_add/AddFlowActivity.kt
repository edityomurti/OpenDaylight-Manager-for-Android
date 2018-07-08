package com.edityomurti.openflowmanagerapp.ui.flow_add

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.edityomurti.openflowmanagerapp.R
import com.edityomurti.openflowmanagerapp.models.flowtable.flow.Flow
import com.edityomurti.openflowmanagerapp.models.flowtable.flow.FlowDataSent
import com.edityomurti.openflowmanagerapp.models.topology.NodeDataSerializable
import com.edityomurti.openflowmanagerapp.utils.Constants
import com.edityomurti.openflowmanagerapp.utils.RestAdapter
import kotlinx.android.synthetic.main.activity_add_flow.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import android.view.inputmethod.InputMethodManager
import java.net.URLEncoder

class AddFlowActivity : AppCompatActivity() {
    val TAG_SELECT_DEVICE_FRAGMENT = "TAG_SELECT_DEVICE_FRAGMENT"
    val TAG_GENERAL_PROPERTIES_FRAGMENT = "TAG_GENERAL_PROPERTIES"
    val TAG_MATCH_FRAGMENT = "TAG_MATCH_FRAGMENT"
    val TAG_ACTION_FRAGMENT = "TAG_ACTION_FRAGMENT"
    val TAG_REVIEW_FRAGMENT = "TAG_REVIEW_FRAGMENT"

    lateinit var selectDeviceFragment: AddFlowSelectDeviceFragment
    lateinit var generalPropertiesFragment: AddFlowGeneralFragment
    lateinit var matchFragment: AddFlowMatchFragment
    lateinit var actionFragment: AddFlowActionFragment
    lateinit var reviewFragment: AddFlowReviewFragment

    lateinit var nodeList: ArrayList<String>
    var nodeData: NodeDataSerializable? = null

    private var currentPosition = 1
    private val MAX_POSITION = 6

    var newFlow = Flow()

    var dataStatus: Boolean? = false

    var restAdapter: RestAdapter? = null
    var addMode: String? = null

    var titleMode: String = "Add Flow"

    var modeAdd: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_flow)

        title = titleMode
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        addMode = intent.getStringExtra(Constants.ADD_MODE)
        nodeList = intent.getStringArrayListExtra(Constants.NODE_LIST)
        nodeData = intent.getSerializableExtra(Constants.NODE_DATA) as NodeDataSerializable

        modeAdd = addMode == Constants.MODE_ADD

        if(modeAdd){
            currentPosition = 1
            titleMode = "Add Flow"
        } else {
            currentPosition = 2
            titleMode = "Edit Flow"
            newFlow = intent.extras.getSerializable(Constants.OBJECT_FLOW) as Flow
        }
        selectDeviceFragment = AddFlowSelectDeviceFragment.newInstance(nodeList)
        generalPropertiesFragment = AddFlowGeneralFragment()
        matchFragment = AddFlowMatchFragment.newInstance(nodeData!!)
        actionFragment = AddFlowActionFragment.newInstance(nodeData!!)
        reviewFragment = AddFlowReviewFragment()
        restAdapter = RestAdapter(this)

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
                title = "$titleMode: Select Device"
                showSelectDevice()
            }
            2 -> {
                println("AddFlow, setNav current == 2")
                if(modeAdd){
                    this.newFlow = selectDeviceFragment.setFlow()
                } else {
                    dataStatus = true
                }

                if(dataStatus!!){
                    if(addMode == Constants.MODE_ADD){
                        btn_previous.visibility = View.VISIBLE
                        btn_next.visibility = View.VISIBLE
                    } else {
                        btn_previous.visibility = View.INVISIBLE
                        btn_next.visibility = View.VISIBLE
                    }
                    tv_next.text = "Next"
                    title = "$titleMode: General Properties"
                    showGeneralProp()
                } else {
                    currentPosition -= 1
                }
            }
            3 -> {
                this.newFlow = generalPropertiesFragment.setFlow()

                if(dataStatus!!){
                    btn_previous.visibility = View.VISIBLE
                    btn_next.visibility = View.VISIBLE
                    showMatch()
                    title = "$titleMode: Match"
                    tv_next.text = "Next"
                } else {
                    currentPosition -= 1
                }
            }
            4 -> {
                this.newFlow = matchFragment.setFlow()
                if(dataStatus!!){
                    btn_previous.visibility = View.VISIBLE
                    btn_next.visibility = View.VISIBLE
                    tv_next.text = "Review"
                    showAction()
                    title = "$titleMode: Action"
                } else {
                    currentPosition -= 1
                }
            }
            5 -> {
                this.newFlow = actionFragment.setFlow()
                if(dataStatus!!){
                    btn_previous.visibility = View.VISIBLE
                    btn_next.visibility = View.VISIBLE
                    tv_next.text = "Finish"
                    title = "$titleMode: Review"
                    showReview()
                } else {
                    currentPosition -= 1
                    Toast.makeText(this, "Something wrong", Toast.LENGTH_SHORT).show()
                }
            }
            6 -> {
                sendFlow()
            }
        }
        val focus = this.currentFocus
        if(focus != null){
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(focus.windowToken, 0)
        }
    }

    fun showSelectDevice(){
        var transaction = supportFragmentManager.beginTransaction()
//        transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
        if(supportFragmentManager.findFragmentByTag(TAG_GENERAL_PROPERTIES_FRAGMENT) != null){
            transaction.hide(supportFragmentManager.findFragmentByTag(TAG_GENERAL_PROPERTIES_FRAGMENT))
        }
        if(supportFragmentManager.findFragmentByTag(TAG_MATCH_FRAGMENT) != null){
            transaction.hide(supportFragmentManager.findFragmentByTag(TAG_MATCH_FRAGMENT))
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
        if(supportFragmentManager.findFragmentByTag(TAG_MATCH_FRAGMENT) != null){
            transaction.hide(supportFragmentManager.findFragmentByTag(TAG_MATCH_FRAGMENT))
        }

        if(supportFragmentManager.findFragmentByTag(TAG_GENERAL_PROPERTIES_FRAGMENT) != null){
            transaction.show(supportFragmentManager.findFragmentByTag(TAG_GENERAL_PROPERTIES_FRAGMENT))
            generalPropertiesFragment.setData()
        }else {
            transaction.add(R.id.fragment_container, generalPropertiesFragment, TAG_GENERAL_PROPERTIES_FRAGMENT)
            val focus = this.currentFocus
            if(focus != null){
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(focus.windowToken, 0)
            }
        }
        transaction.commitAllowingStateLoss()
    }

    fun showMatch(){
        var transaction = supportFragmentManager.beginTransaction()
        if(supportFragmentManager.findFragmentByTag(TAG_SELECT_DEVICE_FRAGMENT) != null){
            transaction.hide(supportFragmentManager.findFragmentByTag(TAG_SELECT_DEVICE_FRAGMENT))
        }
        if(supportFragmentManager.findFragmentByTag(TAG_GENERAL_PROPERTIES_FRAGMENT) != null){
            transaction.hide(supportFragmentManager.findFragmentByTag(TAG_GENERAL_PROPERTIES_FRAGMENT))
        }
        if(supportFragmentManager.findFragmentByTag(TAG_ACTION_FRAGMENT) != null){
            transaction.hide(supportFragmentManager.findFragmentByTag(TAG_ACTION_FRAGMENT))
        }

        if(supportFragmentManager.findFragmentByTag(TAG_MATCH_FRAGMENT) != null){
            transaction.show(supportFragmentManager.findFragmentByTag(TAG_MATCH_FRAGMENT))
            matchFragment.setData()
        } else {
            transaction.add(R.id.fragment_container, matchFragment, TAG_MATCH_FRAGMENT)
        }
        transaction.commitAllowingStateLoss()
    }

    fun showAction(){
        var transaction = supportFragmentManager.beginTransaction()
        if(supportFragmentManager.findFragmentByTag(TAG_MATCH_FRAGMENT) != null){
            transaction.hide(supportFragmentManager.findFragmentByTag(TAG_MATCH_FRAGMENT))
        }
        if(supportFragmentManager.findFragmentByTag(TAG_REVIEW_FRAGMENT) != null){
            transaction.hide(supportFragmentManager.findFragmentByTag(TAG_REVIEW_FRAGMENT))
        }

        if(supportFragmentManager.findFragmentByTag(TAG_ACTION_FRAGMENT) != null){
            transaction.show(supportFragmentManager.findFragmentByTag(TAG_ACTION_FRAGMENT))
            actionFragment.setData()
        } else {
            transaction.add(R.id.fragment_container, actionFragment, TAG_ACTION_FRAGMENT)
        }
        transaction.commitAllowingStateLoss()
    }

    fun showReview(){
        var transaction = supportFragmentManager.beginTransaction()
        if(supportFragmentManager.findFragmentByTag(TAG_ACTION_FRAGMENT) != null){
            transaction.hide(supportFragmentManager.findFragmentByTag(TAG_ACTION_FRAGMENT))
        }

        if(supportFragmentManager.findFragmentByTag(TAG_REVIEW_FRAGMENT) != null){
            transaction.show(supportFragmentManager.findFragmentByTag(TAG_REVIEW_FRAGMENT))
            reviewFragment.setData()
        } else {
            transaction.add(R.id.fragment_container, reviewFragment, TAG_REVIEW_FRAGMENT)
        }
        transaction.commitAllowingStateLoss()
    }

    fun getDataStatus() : Boolean{
        return this.dataStatus!!
    }

    fun setDataStatus(isCompleted: Boolean){
        this.dataStatus = isCompleted
    }

    fun getFlow(): Flow{
        return this.newFlow
    }

    fun setFlow(newFlow: Flow){
        this.newFlow = newFlow
    }

    fun getMode(): Boolean{
        return this.modeAdd
    }

    fun onClickNavigation(){
        setNavigation()
    }

    fun sendFlow(){
        var progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Applying Flow ..")
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()

        var nodeId = newFlow.nodeId
        newFlow.nodeId = null

        if(!modeAdd){
            nodeId = URLEncoder.encode(nodeId, "utf-8")
            println("ENCODING FLOW NODE ID to : ${nodeId}")
        }

        var flowData: MutableList<Flow> = ArrayList()
        flowData.add(newFlow)
        val flowDataSent = FlowDataSent(flowData)

        restAdapter?.getEndPoint()?.
                postFlow(nodeId!!, newFlow.id!!, flowDataSent)!!.
                enqueue(object : retrofit2.Callback<ResponseBody>{
                    override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                        if(response!!.isSuccessful){
                            if(modeAdd){
                                Toast.makeText(this@AddFlowActivity, "Flow Added!", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this@AddFlowActivity, "Flow Updated!", Toast.LENGTH_SHORT).show()
                            }
                            progressDialog.dismiss()
                            setResult(Activity.RESULT_OK)
                            finish()
                        } else {
                            Toast.makeText(this@AddFlowActivity, "Failed Adding Flow", Toast.LENGTH_SHORT).show()
                            newFlow.nodeId = nodeId
                            progressDialog.dismiss()
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                        Toast.makeText(this@AddFlowActivity, "Failed Adding Flow", Toast.LENGTH_SHORT).show()
                        newFlow.nodeId = nodeId
                        progressDialog.dismiss()
                    }
                })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}