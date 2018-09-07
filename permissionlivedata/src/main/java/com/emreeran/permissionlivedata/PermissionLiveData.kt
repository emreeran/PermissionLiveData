package com.emreeran.permissionlivedata

import android.annotation.TargetApi
import android.os.Build
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData


/**
 * Created by Emre Eran on 3.09.2018.
 */
class PermissionLiveData : MediatorLiveData<Permission>() {

    internal lateinit var permissionLiveDataFragment: Lazy<PermissionLiveDataFragment>

    @SuppressWarnings("WeakerAccess", "unused") // Public API
    fun isGranted(permission: String): Boolean {
        return !isMarshmallow() || permissionLiveDataFragment.value.isGranted(permission)
    }

    @SuppressWarnings("WeakerAccess", "unused") // Public API
    fun isRevoked(permission: String): Boolean {
        return isMarshmallow() && permissionLiveDataFragment.value.isRevoked(permission)
    }

    private fun request(vararg permissions: String): PermissionLiveData {
        if (permissions.isEmpty()) {
            throw IllegalArgumentException("PermissionLiveData request requires at least one permission parameter.")
        }

        val list = ArrayList<MutableLiveData<Permission>>(permissions.size)
        val unrequestedPermissions = ArrayList<String>()

        for (permission in permissions) {
            if (isGranted(permission)) {
                val grantedPermission = MutableLiveData<Permission>()
                grantedPermission.value = Permission(
                        name = permission,
                        granted = true,
                        status = Status.RECEIVED
                )

                // Return granted permission object
                list.add(grantedPermission)
                continue
            }

            if (isRevoked(permission)) {
                // Return denied permission object
                val revokedPermission = MutableLiveData<Permission>()
                revokedPermission.value = Permission(
                        name = permission,
                        granted = false,
                        status = Status.RECEIVED
                )
                list.add(revokedPermission)
                continue
            }

            var liveData = permissionLiveDataFragment.value.getLiveDataByPermission(permission)
            if (liveData == null) {
                unrequestedPermissions.add(permission)
                liveData = MutableLiveData<Permission>()
                val pendingPermission = Permission(
                        name = permission,
                        granted = false
                )
                liveData.value = pendingPermission
                permissionLiveDataFragment.value.setLiveDataForPermission(permission, liveData)
            }
            list.add(liveData)
        }

        if (!unrequestedPermissions.isEmpty()) {
            val unrequestedPermissionsArray: Array<out String> = unrequestedPermissions.toTypedArray()
            requestPermissionsFromFragment(unrequestedPermissionsArray)
        }

        // Return concat live data from list
        val liveDataMerger = PermissionLiveData()
        for (data in list) {
            liveDataMerger.addSource(data) { liveDataMerger.value = it }
        }

        return liveDataMerger
    }

    private fun getLazyPermissionLiveDataFragmentSingleton(fragmentManager: FragmentManager)
            : Lazy<PermissionLiveDataFragment> {
        return object : Lazy<PermissionLiveDataFragment> {
            private var permissionLiveDataFragment: PermissionLiveDataFragment? = null

            override val value: PermissionLiveDataFragment
                get() {
                    if (permissionLiveDataFragment == null) {
                        permissionLiveDataFragment = getPermissionLiveDataFragment(fragmentManager)
                    }

                    return permissionLiveDataFragment!!
                }

            override fun isInitialized(): Boolean {
                return permissionLiveDataFragment != null
            }
        }
    }

    private fun getPermissionLiveDataFragment(fragmentManager: FragmentManager): PermissionLiveDataFragment {
        var permissionLiveDataFragment = findPermissionLiveDataFragment(fragmentManager)
        if (permissionLiveDataFragment == null) {
            permissionLiveDataFragment = PermissionLiveDataFragment()
            fragmentManager
                    .beginTransaction()
                    .add(permissionLiveDataFragment, FRAGMENT_TAG)
                    .commitNow()
        }
        return permissionLiveDataFragment
    }

    private fun findPermissionLiveDataFragment(fragmentManager: FragmentManager): PermissionLiveDataFragment? {
        return fragmentManager.findFragmentByTag(FRAGMENT_TAG) as PermissionLiveDataFragment?
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun requestPermissionsFromFragment(permissions: Array<out String>) {
        permissionLiveDataFragment.value.requestPermissions(permissions)
    }

    private fun isMarshmallow(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    companion object {
        internal const val FRAGMENT_TAG = "PermissionLiveDataFragment"

        @JvmStatic
        @SuppressWarnings("unused") // Public API
        fun create(fragment: Fragment, vararg permissions: String): PermissionLiveData {
            val permissionLiveData = PermissionLiveData()
            permissionLiveData.permissionLiveDataFragment =
                    permissionLiveData.getLazyPermissionLiveDataFragmentSingleton(fragment.childFragmentManager)
            return permissionLiveData.request(*permissions)
        }

        @JvmStatic
        @SuppressWarnings("unused") // Public API
        fun create(activity: FragmentActivity, vararg permissions: String): PermissionLiveData {
            val permissionLiveData = PermissionLiveData()
            permissionLiveData.permissionLiveDataFragment =
                    permissionLiveData.getLazyPermissionLiveDataFragmentSingleton(activity.supportFragmentManager)
            return permissionLiveData.request(*permissions)
        }
    }
}
