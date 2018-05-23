package com.stacklearning.mypermissionapp

import android.Manifest
import android.os.Bundle
import com.stacklearning.mypermissionlibrary.MyPermission
import com.stacklearning.mypermissionlibrary.onPermisssionResult

class MainActivity : MyPermission() , onPermisssionResult {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var permissionList = ArrayList<String>()
        permissionList.add( Manifest.permission.CAMERA)
        permissionList.add( Manifest.permission.WRITE_EXTERNAL_STORAGE)
        setPermission(this,permissionList,this)
    }

    override fun onPermissionGranted() {

        // open camera() and ...
        // ... etc what you want to do as per permission
    }


}
