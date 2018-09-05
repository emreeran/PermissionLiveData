package com.emreeran.permissionlivedata

import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData


/**
 * Created by Emre Eran on 3.09.2018.
 */
internal class PermissionLiveDataFragment : Fragment() {

    private val liveDataMap = HashMap<String, MutableLiveData<Permission>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode != PERMISSIONS_REQUEST_CODE) return

        val shouldShowRequestPermissionRationale = BooleanArray(permissions.size)

        for (i in 0 until permissions.size) {
            shouldShowRequestPermissionRationale[i] = shouldShowRequestPermissionRationale(permissions[i])
        }

        onRequestPermissionsResult(permissions, grantResults, shouldShowRequestPermissionRationale)
    }

    @TargetApi(Build.VERSION_CODES.M)
    internal fun requestPermissions(permissions: Array<out String>) {
        requestPermissions(permissions, PERMISSIONS_REQUEST_CODE)
    }

    internal fun getLiveDataByPermission(permission: String): MutableLiveData<Permission>? {
        return liveDataMap[permission]
    }

    internal fun containsPermission(permission: String): Boolean {
        return liveDataMap.containsKey(permission)
    }

    internal fun setLiveDataForPermission(permission: String, liveData: MutableLiveData<Permission>) {
        liveDataMap[permission] = liveData
    }

    @TargetApi(Build.VERSION_CODES.M)
    internal fun isGranted(permission: String): Boolean {
        activity?.let {
            return it.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
        }

        throw IllegalStateException("PermissionLiveDataFragment must be attached to an Activity")
    }

    @TargetApi(Build.VERSION_CODES.M)
    internal fun isRevoked(permission: String): Boolean {
        activity?.let {
            return it.packageManager.isPermissionRevokedByPolicy(permission, it.packageName)
        }

        throw IllegalStateException("PermissionLiveDataFragment must be attached to an Activity")
    }

    internal fun onRequestPermissionsResult(
            permissions: Array<out String>,
            grantResults: IntArray,
            shouldShowRequestPermissionRationale: BooleanArray
    ) {
        for (i in 0 until permissions.size) {
            val liveData = liveDataMap[permissions[i]]
            liveData?.let {
                liveDataMap.remove(permissions[i])
                val granted = grantResults[i] == PackageManager.PERMISSION_GRANTED
                it.postValue(Permission(permissions[i], granted, shouldShowRequestPermissionRationale[i]))
            }
        }
    }

    companion object {
        const val PERMISSIONS_REQUEST_CODE = 101
    }
}