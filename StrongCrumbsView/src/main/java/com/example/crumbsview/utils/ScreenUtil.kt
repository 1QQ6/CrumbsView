package com.example.crumbsview.utils

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.WindowManager

/**
 * @author wangyiqing
 * time:2021/6/9
 * description:屏幕util，获取屏幕宽高等
 */
object ScreenUtil {
    /**
     * 取得屏幕分辨率
     * @param context
     * @return
     */
    fun getScreenResolution(context: Context): Point {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        return Point(display.width, display.height)
    }

    /**
     * 获取屏幕宽度
     * @param context
     * @return
     */
    fun getScreenWidth(context: Context): Int {
        return getScreenResolution(context).x
    }

    /**
     * 获取屏幕高度
     * @param context
     * @return
     */
    fun getScreenHeight(context: Context): Int {
        return getScreenResolution(context).y
    }

    /**
     * dp转px
     * @param context
     * @param dipValue
     * @return
     */
    fun dip2px(context: Context, dipValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

    /**
     * px转dp
     * @param context
     * @param pxValue
     * @return
     */
    fun px2dip(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale - 0.5f).toInt()
    }

    /**
     * 获取应用区的高度
     * @param context
     * @return
     */
    fun getAppScreenHeight(context: Activity): Int {
        val outRect = Rect()
        context.window.decorView.getWindowVisibleDisplayFrame(outRect)
        return outRect.height()
    }

    /**
     * 获取屏幕像素
     */
    fun getDensity(context: Context): Float {
        val mWindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        mWindowManager.defaultDisplay.getMetrics(dm)
        return dm.density
    }
}