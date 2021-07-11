package com.example.crumbsview

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.crumbs.R
import com.example.crumbsview.utils.ScreenUtil
import java.util.*

/**
 * @author wangyiqing
 * time:2021/6/9
 * description:面包屑view
 */
class StrongCrumbsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var mContext: Context? = null
    private val tabList = LinkedList<Tab>()
    private var recyclerView: RecyclerView? = null
    private var tabAdapter: TabAdapter? = null
    private var onTabListener: OnTabListener? = null

    /**
     * 已选中的面包屑item颜色
     */
    private var mBreadCrumbsSelectedColor = Color.parseColor("#8A000000")

    /**
     * 未选中的面包屑item颜色
     */
    private var mBreadCrumbsUnSelectedColor = Color.parseColor("#327EFF")
    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val attributes = context.obtainStyledAttributes(
            attrs, R.styleable.BreadCrumbsView,
            defStyleAttr, 0
        )
        mBreadCrumbsSelectedColor = attributes.getColor(
            R.styleable.BreadCrumbsView_select_item_color,
            mBreadCrumbsSelectedColor
        )
        mBreadCrumbsUnSelectedColor = attributes.getColor(
            R.styleable.BreadCrumbsView_unselected_item_color,
            mBreadCrumbsUnSelectedColor
        )
        mContext = context
        val view = inflate(context, R.layout.layout_breadcrumbs_container, null)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView?.setLayoutManager(
            LinearLayoutManager(
                mContext,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        )
        val maxWidth = (ScreenUtil.getScreenWidth(mContext!!) - ScreenUtil.dip2px(
            mContext!!,
            16f
        )) / 2.0f - ScreenUtil.dip2px(mContext!!, 8f)
        tabAdapter = TabAdapter(mContext!!, tabList, OnClickListener { v ->
            val index = v.tag as Int
            selectAt(index)
        }, maxWidth)
        recyclerView?.adapter = tabAdapter
        addView(view)
    }

    /**
     * 添加新的面包屑
     *
     * @param tab
     */
    private fun addTab(tab: Tab) {
        // 修改tab的状态
        if (!tabList.isEmpty()) {
            tabList.last.isCurrent = false
        }
        // 设置新增tab的数据及状态
        // 设置角标
        tab.index = currentIndex + 1
        // 设置状态
        tab.isCurrent = true
        tabList.add(tab)
        if (onTabListener != null) {
            onTabListener!!.onAdded(tab)
        }
        tabAdapter!!.notifyDataSetChanged()
    }

    /**
     * 添加新的面包屑
     *
     * @param content
     */
    fun addTab(content: String?, value: HashMap<String, String>) {
        val tab = Tab()
        tab.content = content
        tab.value = value
        addTab(tab)
    }

    /**
     * 非active状态的Tab支持选中事件
     *
     * @param index
     */
    fun selectAt(index: Int) {
        // 判断角标越界，当前的tab不可点击
        if (!tabList.isEmpty() && tabList.size > index && !tabList[index].isCurrent) {
            // 移除后面的tab数据
            val size = tabList.size
            // 从尾部开始删除
            for (i in size - 1 downTo index + 1) {
                if (onTabListener != null) {
                    onTabListener!!.onRemoved(tabList.last)
                }
                tabList.removeLast()
            }
            // 修改当前选中的tab状态
            tabList.last.isCurrent = true
            if (onTabListener != null) {
                onTabListener!!.onActivated(tabList.last)
            }
            tabAdapter!!.notifyDataSetChanged()
        }
    }

    val currentIndex: Int
        get() = tabList.size - 1

    fun setOnTabListener(onTabListener: OnTabListener?) {
        this.onTabListener = onTabListener
    }

    val lastTab: Tab?
        get() = if (!tabList.isEmpty()) {
            tabList.last
        } else null

    class Tab {
        private val mContext: Context? = null

        /**
         * 是否当前tab，非当前tab才可点击
         */
        var isCurrent = false

        /**
         * 角标，从0开始
         */
        var index = 0

        /**
         * 面包屑内容
         */
        var content: String? = null

        /**
         * 业务携带的参数
         */
        var value: Map<String, String>? = null
    }

    private inner class TabAdapter(
        private val mContext: Context,
        private val list: List<Tab>?,
        private val onItemClickListener: OnClickListener?,
        private val maxItemWidth: Float
    ) : RecyclerView.Adapter<TabAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val v = LayoutInflater.from(mContext)
                .inflate(R.layout.layout_breadcrumbs_tab, parent, false)
            return ViewHolder(v)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = list!![position]
            holder.tv.text = item.content
            if (item.isCurrent) {
                holder.tv.setTextColor(mBreadCrumbsSelectedColor)
                holder.iv.visibility = GONE
                holder.itemView.setOnClickListener(null)
            } else {
                holder.tv.setTextColor(mBreadCrumbsUnSelectedColor)
                holder.iv.visibility = VISIBLE
                holder.itemView.setOnClickListener { v ->
                    if (onItemClickListener != null) {
                        v.tag = position
                        onItemClickListener.onClick(v)
                    }
                }
            }
            if (maxItemWidth > 0) {
                val width: Int
                width = if (item.isCurrent) {
                    maxItemWidth.toInt()
                } else {
                    maxItemWidth.toInt() - ScreenUtil.dip2px(mContext, 24f)
                }
                holder.tv.maxWidth = width
            }
        }

        override fun getItemCount(): Int {
            return list?.size ?: 0
        }

        internal inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tv: TextView
            val iv: ImageView

            init {
                tv = itemView.findViewById(R.id.tv_content)
                iv = itemView.findViewById(R.id.iv_arrow)
            }
        }
    }

    interface OnTabListener {
        /**
         * 新添加进来的回调
         *
         * @param tab
         */
        fun onAdded(tab: Tab?)

        /**
         * 再次显示到栈顶的回调
         *
         * @param tab
         */
        fun onActivated(tab: Tab?)

        /**
         * 被移除的回调
         *
         * @param tab
         */
        fun onRemoved(tab: Tab?)
    }

    init {
        init(context, attrs, defStyleAttr)
    }
}