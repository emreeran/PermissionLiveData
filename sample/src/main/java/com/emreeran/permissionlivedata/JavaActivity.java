package com.emreeran.permissionlivedata;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import timber.log.Timber;

/**
 * Created by Emre Eran on 5.09.2018.
 */
@SuppressLint("Registered") // Java usage sample
public class JavaActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        PermissionLiveData permissionLiveData = PermissionLiveData.create(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA
        );

        permissionLiveData.observe(this, permission -> {
            if (permission.getGranted()) {
                Timber.d("Permission " + permission.getName() + " was granted.");
            } else if (permission.getShouldShowRequestPermissionRationale()) {
                Timber.d("Permission " + permission.getName() + " was denied without ask never again checked.");
            } else {
                Timber.d("Permission " + permission.getName() + " was denied.");
            }
        });
    }
}
