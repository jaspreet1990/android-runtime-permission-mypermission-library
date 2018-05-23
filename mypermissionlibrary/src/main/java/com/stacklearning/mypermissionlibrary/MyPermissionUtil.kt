package com.stacklearning.mypermissionlibrary

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import java.util.*


internal abstract class MyPermissionUtil(var activity: Activity) {

    val status: ArrayList<statusArray>
        get() {
            val statusPermission = ArrayList<statusArray>()
            val grantPermissionList = ArrayList<String>()
            val denyPermissionList = ArrayList<String>()
            val listPermissionsNeeded = setPermission()
            for (per in listPermissionsNeeded) {
                if (ContextCompat.checkSelfPermission(activity.applicationContext, per) == PackageManager.PERMISSION_GRANTED) {
                    grantPermissionList.add(per)
                } else {
                    denyPermissionList.add(per)
                }
            }
            val stat = statusArray(grantPermissionList, denyPermissionList)
            statusPermission.add(stat)
            return statusPermission
        }

    fun checkAndRequestPermissions(): Boolean {
       // this.activity = activity
        if (Build.VERSION.SDK_INT >= 23) {
            val listPermissionsNeeded = setPermission()
            val listPermissionsAssign = ArrayList<String>()
            for (per in listPermissionsNeeded) {
                if (ContextCompat.checkSelfPermission(activity.applicationContext, per) != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsAssign.add(per)
                }
            }

            if (!listPermissionsAssign.isEmpty()) {
                ActivityCompat.requestPermissions(activity, listPermissionsAssign.toTypedArray(), 1212)
                return false
            }
        }
        return true
    }

    abstract fun setPermission(): List<String>

    fun checkResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            1212 -> {
                val listPermissionsNeeded = setPermission()
                val perms = HashMap<String, Int>()
                for (permission in listPermissionsNeeded) {
                    perms[permission] = PackageManager.PERMISSION_GRANTED
                }
                if (grantResults.size > 0) {
                    for (i in permissions.indices)
                        perms[permissions[i]] = grantResults[i]
                    var isAllGranted = true
                    for (permission in listPermissionsNeeded) {
                        if (perms[permission] == PackageManager.PERMISSION_DENIED) {
                            isAllGranted = false
                            break
                        }
                    }
                    if (isAllGranted) {

                    } else {
                        var shouldRequest = false
                        for (permission in listPermissionsNeeded) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                                shouldRequest = true
                                break
                            }
                        }
                        if (shouldRequest) {
                            cancelAndRequestAgain(activity)
                        } else {
                            showAppSettingBar(activity)
                        }
                    }
                }
            }
        }
    }

    private fun cancelAndRequestAgain(activity: Activity) {

        showDialogOK(activity, "Please grant Permission to use feature of this App",
                DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> checkAndRequestPermissions()
                        DialogInterface.BUTTON_NEGATIVE -> {
                        }
                    }// proceed with logic by disabling the related features or quit the app.
                })
    }

    private fun showAppSettingBar(activity: Activity) {
        showDialogOK(activity, "Go to settings and enable permissions for the App",
                DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> gotoAppSetting(activity)
                        DialogInterface.BUTTON_NEGATIVE -> {
                        }
                    }
                })


    }

    private fun showDialogOK(activity: Activity, message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton("Sure", okListener)
                .setNegativeButton("Not Now", okListener)
                .create()
                .show()
    }

    inner class statusArray internal constructor(var granted: ArrayList<String>, var denied: ArrayList<String>)

    private fun gotoAppSetting(activity: Activity) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", activity.packageName, null))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        activity.startActivity(intent)
    }
}

