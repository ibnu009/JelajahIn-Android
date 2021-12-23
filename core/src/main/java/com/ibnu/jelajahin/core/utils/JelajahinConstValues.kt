package com.ibnu.jelajahin.core.utils

import android.Manifest

object JelajahinConstValues {

    const val BASE_URL = "http://192.168.1.7:8000/"
    const val POST_WISATA_ULASAN_URL = "${BASE_URL}api/wisata/add_ulasan"
    const val EDIT_PROFILE_URL = "${BASE_URL}api/profile/edit"


    const val DEFAULT_PAGE_INDEX = 1
    const val DEFAULT_PAGE_SIZE = 10

    const val BUFFER_SIZE: Int = 1024 * 2

    const val OPEN_WEBVIEW = "open_webview"
    const val OPEN_FRAGMENT = "open_fragment"
    const val FRAGMENT_DETAIL_WISTA = "DetailWisata"
    const val FRAGMENT_DETAIL_EVENT = "DetailEvent"
    const val FRAGMENT_DETAIL_HOTEL = "DetailHotel"
    const val FRAGMENT_DETAIL_RESTAURANT = "DetailRestaurant"

    const val RESTAURANT_MARKER = "restaurant.marker.map"
    const val EVENT_MARKER = "event.marker.map"
    const val GEM_MARKER = "gem.marker.map"

    const val PREFS_NAME = "jelajahin.pref"
    const val KEY_TOKEN = "key.token"
    const val KEY_IS_ALREADY_INTRODUCED = "key.is.already.introduced"

    val LOCATION_PERMISSION = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
    const val RC_LOCATION = 123
}