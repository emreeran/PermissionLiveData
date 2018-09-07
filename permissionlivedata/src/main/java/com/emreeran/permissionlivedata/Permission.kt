package com.emreeran.permissionlivedata

/**
 * Created by Emre Eran on 3.09.2018.
 */
data class Permission(
        val name: String,
        val granted: Boolean,
        val shouldShowRequestPermissionRationale: Boolean = false,
        val status: Status = Status.PENDING
)

enum class Status {
    PENDING,
    RECEIVED
}
