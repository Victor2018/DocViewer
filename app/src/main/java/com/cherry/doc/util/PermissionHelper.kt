package com.cherry.doc.util

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment


/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: PermissionHelper
 * Author: Victor
 * Date: 2022/5/6 10:11
 * Description: 
 * -----------------------------------------------------------------
 */

object PermissionHelper {
    /**
     * 权限检测
     * @param permission
     * @return
     */
    fun hasPermission(context: Context, permission: String?): Boolean {
        if (TextUtils.isEmpty(permission)) return false
        var hasPermission = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, permission!!)
                != PackageManager.PERMISSION_GRANTED
            ) {
                hasPermission = false
            }
        }
        return hasPermission
    }

    fun requestPermission(activity: Activity, permissions: Array<String>, requestCode: Int) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode)
    }

    fun requestPermission(fragment: Fragment, permissions: Array<String>, requestCode: Int) {
        fragment.requestPermissions(permissions, requestCode)
    }

    // 判断是否打开了通知监听权限
    fun isNotificationEnabled(context: Context): Boolean {
        return try {
            NotificationManagerCompat.from(context).areNotificationsEnabled()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    //跳转到app详情打开通知设置
    fun openNotification(context: Context) {
        val intent = Intent()
        if (Build.VERSION.SDK_INT >= 26) {
            // android 8.0引导
            intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
            intent.putExtra("android.provider.extra.APP_PACKAGE", context.packageName)
        } else if (Build.VERSION.SDK_INT >= 21) {
            // android 5.0-7.0
            intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
            intent.putExtra("app_package",  context.packageName)
            intent.putExtra("app_uid",  context.applicationInfo.uid)
        } else {
            // 其他
            intent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
            intent.data = Uri.fromParts("package", context.packageName, null)
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

}