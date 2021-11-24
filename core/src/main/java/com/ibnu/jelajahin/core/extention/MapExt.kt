package com.ibnu.jelajahin.core.extention

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.ibnu.jelajahin.core.R
import com.ibnu.jelajahin.core.utils.JelajahinConstValues.EVENT_MARKER


fun GoogleMap.addSingleMarker(location: LatLng, markerName: String, type: String, uuid: String){
    this.addMarker(createMarkerOptions(location, markerName, type))?.tag = uuid
}

private fun createMarkerOptions(location: LatLng, markerName: String, markerType: String) : MarkerOptions{
    var iconDrawable = 0
    when(markerType){
        EVENT_MARKER -> {
            iconDrawable = R.drawable.ic_event_marker
        }
        else -> {
            iconDrawable = R.drawable.ic_event_marker
        }
    }
    val icon = BitmapDescriptorFactory.fromResource(iconDrawable)

    return MarkerOptions().position(location)
        .title(markerName)
        .icon(icon)

}

