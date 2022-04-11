package com.ibnu.jelajahin.core.extention.map

import android.content.Context
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ui.IconGenerator
import com.ibnu.jelajahin.core.R
import com.ibnu.jelajahin.core.data.model.Gem
import com.ibnu.jelajahin.core.data.model.Penginapan
import com.ibnu.jelajahin.core.data.model.Restaurant
import com.ibnu.jelajahin.core.data.model.Wisata
import com.ibnu.jelajahin.core.utils.JelajahinConstValues.EVENT_MARKER
import com.ibnu.jelajahin.core.utils.JelajahinConstValues.GEM_MARKER


fun GoogleMap.addSingleMarker(
    location: LatLng,
    markerName: String,
    type: String,
    uuid: String,
    context: Context
) {
    this.addMarker(createMarkerOptions(location, markerName, type, context))?.tag = uuid
}

fun GoogleMap.addMultipleMarkersForWisata(listWisata: List<Wisata>, context: Context) {
    for (wisata in listWisata) {
        val marker = this.addMarker(
            createMarkerOptions(
                LatLng(wisata.latitude, wisata.longitude),
                wisata.name,
                "wisata",
                context
            )
        )
        marker?.tag = wisata
        marker?.showInfoWindow()
    }
}

fun GoogleMap.addMultipleMarkersForRestaurant(listRestaurant: List<Restaurant>, context: Context) {
    for (restaurant in listRestaurant) {
        val marker = this.addMarker(
            createMarkerOptions(
                LatLng(restaurant.latitude, restaurant.longtitude),
                "Rp ${restaurant.priceMin} - Rp ${restaurant.priceMax}",
                "wisata",
                context
            )
        )
        marker?.tag = restaurant
        marker?.showInfoWindow()
    }
}

fun GoogleMap.addMultipleMarkersForPenginapan(listPenginapan: List<Penginapan>, context: Context) {
    for (penginapan in listPenginapan) {
        val marker = this.addMarker(
            createMarkerOptions(
                LatLng(penginapan.latitude, penginapan.longitude),
                "Rp ${penginapan.priceMin} - Rp ${penginapan.priceMax}",
                "penginapan",
                context
            )
        )
        marker?.tag = penginapan
        marker?.showInfoWindow()
    }
}

fun GoogleMap.addMultipleMarkersForGem(listGem: List<Gem>, context: Context) {
    for (gem in listGem) {
        this.addMarker(
            createMarkerOptions(
                LatLng(gem.latitude, gem.longitude),
                gem.name,
                GEM_MARKER,
                context
            )
        )
    }
}

fun GoogleMap.animateCameraToSingleMarker(location: LatLng) {
    val zoomLevel = 18.0f
    val cu = CameraUpdateFactory.newLatLngZoom(location, zoomLevel)
    this.animateCamera(cu, 1000, null)
}

fun GoogleMap.boundsCameraToMarkers(locations: List<LatLng>) {
    val builder = LatLngBounds.builder()
    for (location in locations) {
        builder.include(location)
    }
    val bounds = builder.build()
    val zoomLevel = 50
    val cu = CameraUpdateFactory.newLatLngBounds(bounds, zoomLevel)
    this.animateCamera(cu, 1000, null)
}

private fun createMarkerOptions(
    location: LatLng,
    markerName: String,
    markerType: String,
    context: Context
): MarkerOptions {
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
            val bubbleFactory = IconGenerator(context)
            bubbleFactory.setStyle(IconGenerator.STYLE_GREEN)
            val iconBitmap = bubbleFactory.makeIcon(markerName)
            val icon = BitmapDescriptorFactory.fromBitmap(iconBitmap)
            MarkerOptions().position(location).icon(icon)
        }
    }
}
