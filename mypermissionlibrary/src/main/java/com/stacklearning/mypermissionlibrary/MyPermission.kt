package com.stacklearning.mypermissionlibrary


import android.app.Activity
import android.support.v7.app.AppCompatActivity

  open class MyPermission : AppCompatActivity() {

    lateinit var listener: onPermisssionResult
    internal lateinit var permissionUtil: MyPermissionUtil
    var customPermissionSize: Int = 0

     fun setPermission(listener: onPermisssionResult, customPermission: List<String>,activity: Activity) {

        this.listener = listener

        permissionUtil = object : MyPermissionUtil(activity) {

            override fun setPermission(): List<String> {
                customPermissionSize = customPermission.size
                return customPermission
            }
        }
        checkAndRequestPermissions()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        permissionUtil.checkResult(requestCode, permissions, grantResults)
        val granted = permissionUtil.status[0].granted
        val denied = permissionUtil.status[0].denied

        if (customPermissionSize == granted.size) {
            onPermissionGranted()
        }
    }

   private fun checkAndRequestPermissions() {
        val isPermissionGiven: Boolean = permissionUtil.checkAndRequestPermissions()
        if (isPermissionGiven) {
            onPermissionGranted()
        }
    }

    private fun onPermissionGranted() {
        listener.onPermissionGranted()

    }
}