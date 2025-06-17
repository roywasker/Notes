package com.example.notesmoveo.utils

import android.location.Location

interface LocationProvider {
    suspend fun getCurrentLocation(): Location?
}