package com.emreeran.permissionlivedata

/**
 * Created by Emre Eran on 3.09.2018.
 */
data class Permission(
        val name: String,
        val granted: Boolean,
        val shouldShowRequestPermissionRationale: Boolean = false
) {

    constructor(permissions: List<Permission>) :
            this(
                    combineName(permissions),
                    combineGranted(permissions),
                    combineShouldShowRequestPermissionRationale(permissions)
            )

    companion object {
        private fun combineName(permissions: List<Permission>): String {
            val builder = StringBuilder()
            for (permission in permissions) {
                val name = permission.name
                if (builder.isEmpty()) {
                    builder.append(name)
                } else {
                    builder.append(", ").append(name)
                }
            }
            return builder.toString()
        }

        private fun combineGranted(permissions: List<Permission>): Boolean {
            var result = true
            for (permission in permissions) {
                result = result && permission.granted
            }
            return result
        }

        private fun combineShouldShowRequestPermissionRationale(permissions: List<Permission>): Boolean {
            var result = true
            for (permission in permissions) {
                result = result && permission.shouldShowRequestPermissionRationale
            }
            return result
        }
    }

}