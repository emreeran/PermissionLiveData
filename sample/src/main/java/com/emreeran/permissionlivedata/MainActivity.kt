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

        val data = PermissionLiveData(this).request(
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION
        )

        data.observe(this, Observer {
            Timber.d("Permission received: $it")
        })
    }
}
