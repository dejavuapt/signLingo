package com.rsll.signlingo

import android.os.Build


object Constants {

    const val TAG = "CameraX"
    const val FILE_NAME_FORMAT = "yy-MM-dd-HH-mm-ss-SSS"
    const val REQUEST_CODE_PERMISSIONS = 10
    val REQUIRED_PERMISSIONS = mutableListOf (
        android.Manifest.permission.CAMERA,
    ).apply {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }.toTypedArray()
}