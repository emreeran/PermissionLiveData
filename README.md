# PermissionLiveData

Android runtime permissions as ```LiveData```.

For more information on ```LiveData``` refer to this [link][livedata].

### Setup

Add repository
```
repositories {
    maven {
        url  "https://dl.bintray.com/emre/maven"
    }
}
```

Add lifecycle and ```permissionlivedata``` to your dependencies:

```
dependencies {

    implementation "androidx.lifecycle:lifecycle-runtime:2.0.0-rc01"
    implementation "androidx.lifecycle:lifecycle-extensions:2.0.0-rc01"
    implementation "androidx.lifecycle:lifecycle-common-java8:2.0.0-rc01"
    kapt "androidx.lifecycle:lifecycle-compiler:2.0.0-rc01"

    implementation "com.emreeran.permissionlivedata:permissionlivedata:1.0.2"

    ...
}
```

Add desired permissions to your manifest.

### Usage

In Kotlin:

```
val permissionLiveData = PermissionLiveData.create(
        this,                                       // this being an activity or a fragment
        Manifest.permission.CAMERA,                 // Permissions to request
        Manifest.permission.ACCESS_FINE_LOCATION
)

permissionLiveData.observe(this, Observer {
    when {
        it.granted -> Timber.d("Permission ${it.name} was granted.")
        it.shouldShowRequestPermissionRationale ->
            Timber.d("Permission ${it.name} was denied without ask never again checked.")
        else -> Timber.d("Permission ${it.name} was denied.")
    }
})
```

In Java

```
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
```

[livedata]: https://developer.android.com/topic/libraries/architecture/livedata

### License

```
Copyright 2018 Emre Eran

Licensed to the Apache Software Foundation (ASF) under one or more contributor
license agreements.  See the NOTICE file distributed with this work for
additional information regarding copyright ownership.  The ASF licenses this
file to you under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License.  You may obtain a copy of
the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
License for the specific language governing permissions and limitations under
the License.
```