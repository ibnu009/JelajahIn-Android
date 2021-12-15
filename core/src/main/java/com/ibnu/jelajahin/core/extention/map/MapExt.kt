package com.ibnu.jelajahin.core.extention.map

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.ibnu.jelajahin.core.R
import com.ibnu.jelajahin.core.data.model.Gem
import com.ibnu.jelajahin.core.data.model.Wisata
import com.ibnu.jelajahin.core.utils.JelajahinConstValues.EVENT_MARKER
import com.ibnu.jelajahin.core.utils.JelajahinConstValues.GEM_MARKER


fun GoogleMap.addSingleMarker(location: LatLng, markerName: String, type: String, uuid: String){
    this.addMarker(createMarkerOptions(location, markerName, type))?.tag = uuid
}

fun GoogleMap.addMultipleMarkersForWisata(listWisata: List<Wisata>) {
    for (wisata in listWisata) {
        this.addMarker(createMarkerOptions(LatLng(wisata.latitude, wisata.longitude), wisata.name, "wisata"))
    }
}

fun GoogleMap.addMultipleMarkersForGem(listGem: List<Gem>) {
    for (gem in listGem) {
        this.addMarker(createMarkerOptions(LatLng(gem.latitude, gem.longitude), gem.name, GEM_MARKER))
    }
}

fun GoogleMap.animateCameraToSingleMarker(location: LatLng){
    val zoomLevel = 18.0f
    val cu = CameraUpdateFactory.newLatLngZoom(location, zoomLevel)
    this.animateCamera(cu, 1000, null)
}

fun GoogleMap.boundsCameraToMarkers(locations: List<LatLng>){
    val builder = LatLngBounds.builder()
    for (location in locations){
        builder.include(location)
    }
    val bounds = builder.build()
    val zoomLevel = 50
    val cu = CameraUpdateFactory.newLatLngBounds(bounds, zoomLevel)
    this.animateCamera(cu, 1000, null)
}

private fun createMarkerOptions(location: LatLng, markerName: String, markerType: String) : MarkerOptions{
    return when (markerType) {
        EVENT_MARKER -> {
            val iconDrawable = R.drawable.ic_event_marker
            val icon = BitmapDescriptorFactory.fromResource(iconDrawable)
            MarkerOptions().position(location)
                .title(markerName)
                .icon(icon)
        }
        GEM_MARKER -> {
            val iconDrawable = R.drawable.ic_gem_location
            val icon = BitmapDescriptorFactory.fromResource(iconDrawable)
            MarkerOptions().position(location)
                .title(markerName)
                .icon(icon)
        }
        else -> {
            MarkerOptions().position(location)
                .title(markerName)
        }
    }



}

