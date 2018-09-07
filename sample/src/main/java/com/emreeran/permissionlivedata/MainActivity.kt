package com.emreeran.permissionlivedata

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        Timber.plant(Timber.DebugTree())

        val permissionLiveData = PermissionLiveData.create(
                this,
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION
        )

        permissionLiveData.observe(this, Observer {
            if (it.status == Status.RECEIVED) {
                when {
                    it.granted -> Timber.d("Permission ${it.name} was granted.")
                    it.shouldShowRequestPermissionRationale ->
                        Timber.d("Permission ${it.name} was denied without ask never again checked.")
                    else -> Timber.d("Permission ${it.name} was denied.")
                }
            } else if (it.status == Status.PENDING) {
                Timber.d("Pending request for ${it.name}")
            }
        })
    }
}
