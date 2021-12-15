package com.ibnu.jelajahin.core.extention.map

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.model.DirectionsResult
import timber.log.Timber


class MapDirectionHelper {

    fun getPath(
        geoContext: GeoApiContext,
        myLocation: Location,
        targetLocation: LatLng
    ): List<LatLng> {
        val path = ArrayList<LatLng>()

        val myLocationLat = myLocation.latitude.toString()
        val myLocationLng = myLocation.longitude.toString()
        val targetLocationLat = targetLocation.latitude.toString()
        val targetLocationLng = targetLocation.longitude.toString()
        val req = DirectionsApi.getDirections(
            geoContext,
            "$myLocationLat,$myLocationLng",
            "$targetLocationLat,$targetLocationLng"
        )

        try {
            val res: DirectionsResult = req.await()

            //Loop through legs and steps to get encoded polylines of each step
            if (res.routes != null && res.routes.isNotEmpty()) {
                val route = res.routes[0]
                if (route.legs != null) {
                    for (i in route.legs.indices) {
                        val leg = route.legs[i]
                        if (leg.steps != null) {
                            for (j in leg.steps.indices) {
                                val step = leg.steps[j]
                                if (step.steps != null && step.steps.isNotEmpty()) {
                                    for (k in step.steps.indices) {
                                        val step1 = step.steps[k]
                                        val points1 = step1.polyline
                                        if (points1 != null) {
                                            //Decode polyline and add points to list of route coordinates
                                            val coords1 = points1.decodePath()
                                            for (coord1 in coords1) {
                                                path.add(LatLng(coord1.lat, coord1.lng))
                                            }
                                        }
                                    }
                                } else {
                                    val points = step.polyline
                                    if (points != null) {
                                        //Decode polyline and add points to list of route coordinates
                                        val coords = points.decodePath()
                                        for (coord in coords) {
                                            path.add(LatLng(coord.lat, coord.lng))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (ex: Exception) {
            Timber.e(ex.localizedMessage)
        }
        return path
    }

}
