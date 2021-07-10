package com.example.crumbsview

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import com.example.crumbsview.constants.Constants
import com.example.crumbsview.fragments.FragmentA
import com.example.crumbsview.fragments.FragmentB
import com.example.crumbsview.fragments.FragmentC
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

/**
 * @author wangyiqing
 * time:2021/7/10
 * description:
 */
class FragmentManagerAct : FragmentActivity(), View.OnClickListener {
    private var fragmentA: FragmentA? = null
    private var fragmentB: FragmentB? = null
    private var fragmentC: FragmentC? = null
    private var currentFragment = 0
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initBreadCrumbsView()
        setFragment(currentFragment, true)

        bottom_btn.setOnClickListener {
            when (currentFragment) {
                Constants.TYPE_FRAGMENT_A -> {
                    setFragment(Constants.TYPE_FRAGMENT_B,true)
                }
                Constants.TYPE_FRAGMENT_B -> {
                    setFragment(Constants.TYPE_FRAGMENT_C,true)
                }
                else -> {

                }
            }
        }
    }

    /**
     * 初始化面包屑view
     */
    private fun initBreadCrumbsView() {
        breadCrumbs?.setOnTabListener(object : CrumbsView.OnTabListener {
            override fun onAdded(tab: CrumbsView.Tab?) {

            }
            override fun onActivated(tab: CrumbsView.Tab) {
                setFragment(tab.index, false)
            }

            override fun onRemoved(tab: CrumbsView.Tab?) {

            }
        })
    }

    /**
     * 选中某个Fragment
     *
     * @param index
     * @param isAdd 是否添加面包屑item
     */
    fun setFragment(index: Int, isAdd: Boolean) {
        val mFragmentManager = supportFragmentManager
        val mTransaction =
            mFragmentManager.beginTransaction()
        hideFragments(mTransaction)
        when (index) {
            Constants.TYPE_FRAGMENT_A -> {
                //显示对应Fragment
                if (fragmentA == null) {
                    fragmentA = FragmentA()
                    mTransaction.add(
                        R.id.container, fragmentA!!,
                        "fragmentA"
                    )
                } else {
                    mTransaction.show(fragmentA!!)
                }
            }
            Constants.TYPE_FRAGMENT_B -> {
                if (fragmentB == null) {
                    fragmentB = FragmentB()
                    mTransaction.add(
                        R.id.container, fragmentB!!,
                        "fragmentB"
                    )
                } else {
                    mTransaction.show(fragmentB!!)
                }
            }
            Constants.TYPE_FRAGMENT_C -> {
                if (fragmentC == null) {
                    fragmentC = FragmentC()
                    mTransaction.add(
                        R.id.container, fragmentC!!,
                        "fragmentC"
                    )
                } else {
                    mTransaction.show(fragmentC!!)
                }
            }
            else -> {
            }
        }
        //提交事务
        mTransaction.commitAllowingStateLoss()
        currentFragment = index
        if (isAdd) {
            when (currentFragment) {
                Constants.TYPE_FRAGMENT_A -> addBreadCrumb("标签$currentFragment")
                Constants.TYPE_FRAGMENT_B -> addBreadCrumb("标签$currentFragment")
                Constants.TYPE_FRAGMENT_C -> addBreadCrumb("标签$currentFragment")
                else -> {
                }
            }
        }
    }

    private fun addBreadCrumb(title: String) {
        val sb = StringBuilder(title)
        // 传递参数，这里的map数据会存放在tab上，在创建Fragment时可以通过tab.getValue获取
        val map =
            HashMap<String, String>()
        breadCrumbs!!.addTab(sb.toString(), map)
    }

    //隐藏Fragment
    private fun hideFragments(transaction: FragmentTransaction) {
        if (fragmentA != null) {
            transaction.hide(fragmentA!!)
        }
        if (fragmentB != null) {
            transaction.hide(fragmentB!!)
        }
        if (fragmentC != null) {
            transaction.hide(fragmentC!!)
        }
    }

    override fun onClick(v: View) {

    }
}