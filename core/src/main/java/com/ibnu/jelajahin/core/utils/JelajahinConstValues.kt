package com.ibnu.jelajahin.core.utils

import android.Manifest

object JelajahinConstValues {

    const val BASE_URL = "https://api.jelajahin.xyz/"
//    const val BASE_URL = "https://ef00-2001-448a-5122-d234-bc3f-399b-29f3-7f88.ngrok.io/"
    const val POST_WISATA_ULASAN_URL = "${BASE_URL}api/wisata/add_ulasan"
    const val POST_RESTAURANT_ULASAN_URL = "${BASE_URL}api/restaurant/add_ulasan"
    const val POST_PENGINAPAN_ULASAN_URL = "${BASE_URL}api/penginapan/add_ulasan"

    const val EDIT_PROFILE_URL = "${BASE_URL}api/profile/edit"

    const val DEFAULT_PAGE_INDEX = 1
    const val DEFAULT_PAGE_SIZE = 10

    const val BUFFER_SIZE: Int = 1024 * 2

    const val RECOMMENDATION_WISATA_TYPE = "type wisata"
    const val RECOMMENDATION_RESTAURANT_TYPE = "type restaurant"
    const val RECOMMENDATION_PENGINAPAN_TYPE = "type penginapan"

    const val OPEN_WEBVIEW = "open_webview"
    const val OPEN_FRAGMENT = "open_fragment"

    const val FRAGMENT_DETAIL_WISTA = "DetailWisata"
    const val FRAGMENT_DETAIL_EVENT = "DetailEvent"
    const val FRAGMENT_DETAIL_HOTEL = "DetailHotel"
    const val FRAGMENT_DETAIL_RESTAURANT = "DetailRestaurant"

    const val EVENT_MARKER = "event.marker.map"
    const val GEM_MARKER = "gem.marker.map"

    const val PREFS_NAME = "jelajahin.pref"
    const val KEY_TOKEN = "key.token"
    const val KEY_IS_ALREADY_INTRODUCED = "key.is.already.introduced"

    val LOCATION_PERMISSION = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
}