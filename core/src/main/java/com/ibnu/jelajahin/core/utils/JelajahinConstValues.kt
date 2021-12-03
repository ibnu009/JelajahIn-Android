package com.ibnu.jelajahin.core.utils

import android.Manifest

object JelajahinConstValues {

    const val BASE_URL = "http://192.168.1.12:8000/"

    const val DEFAULT_PAGE_INDEX = 1
    const val DEFAULT_PAGE_SIZE = 10

    const val REQUEST_OPEN_MAP_WISATA = "open.map.wisata"
    const val REQUEST_OPEN_MAP_PENGINAPAN = "open.map.penginapan"
    const val REQUEST_OPEN_MAP_RESTAURANT = "open.map.restaurant"

    const val RESTAURANT_MARKER = "restaurant.marker.map"
    const val EVENT_MARKER = "event.marker.map"

    const val PREFS_NAME = "jelajahin.pref"
    const val KEY_TOKEN = "key.token"
    const val KEY_IS_ALREADY_INTRODUCED = "key.is.already.introduced"

    val LOCATION_PERMISSION = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
    const val RC_LOCATION = 123
}