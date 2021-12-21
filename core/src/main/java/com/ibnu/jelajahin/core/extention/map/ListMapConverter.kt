package com.ibnu.jelajahin.core.extention.map

import com.google.android.gms.maps.model.LatLng
import com.ibnu.jelajahin.core.data.model.Gem
import com.ibnu.jelajahin.core.data.model.Restaurant
import com.ibnu.jelajahin.core.data.model.Wisata


fun List<Wisata>.convertWisataToLatLng(): List<LatLng>{
    val listMarker = ArrayList<LatLng>()
    for (wisata in this){
        listMarker.add(LatLng(wisata.latitude, wisata.longitude))
    }
    return listMarker
}

fun List<Restaurant>.convertRestaurantToLatLng(): List<LatLng>{
    val listMarker = ArrayList<LatLng>()
    for (restaurant in this){
        listMarker.add(LatLng(restaurant.latitude, restaurant.longtitude))
    }
    return listMarker
}

fun List<Gem>.convertGemToLatLng(): List<LatLng>{
    val listMarker = ArrayList<LatLng>()
    for (gem in this){
        listMarker.add(LatLng(gem.latitude, gem.longitude))
    }
    return listMarker
}

