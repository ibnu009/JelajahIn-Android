package com.ibnu.jelajahin.core.ui.gwindow

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.ibnu.jelajahin.core.R
import com.ibnu.jelajahin.core.data.model.Restaurant
import com.ibnu.jelajahin.core.utils.JelajahinConstValues.BASE_URL

class InfoWindowsRestaurant(private val activity: Activity, private val context: Context) : GoogleMap.InfoWindowAdapter {

    @SuppressLint("SetTextI18n")
    override fun getInfoContents(p0: Marker): View {
        val view: View = activity.layoutInflater
            .inflate(R.layout.info_window_jelajahin, null)

        val img = view.findViewById<ImageView>(R.id.imvWindow)
        val name = view.findViewById<TextView>(R.id.tvName)
        val rating = view.findViewById<TextView>(R.id.tvRating)
        val stars = view.findViewById<RatingBar>(R.id.ratingBar)
        val price = view.findViewById<TextView>(R.id.tvPrice)
        val address = view.findViewById<TextView>(R.id.tvAddress)
        val schedule = view.findViewById<TextView>(R.id.tvSchedule)
        val type = view.findViewById<TextView>(R.id.tvType)

        val data = p0.tag as Restaurant

        Glide.with(context)
            .load(BASE_URL+data.imageUrl)
            .into(img)

        name.text = data.name
        rating.text = if (data.ratingAverage != null) data.ratingAverage.toString() else "0.0"
        stars.rating = data.ratingAverage?.toFloat() ?: 0f
        price.text = "Rp ${data.priceMin} - ${data.priceMax}"
        address.text = data.address
        schedule.text = "${data.OpenTime} - ${data.CloseTime}"
        type.text = data.foodType

        return view
    }

    override fun getInfoWindow(p0: Marker): View? {


        return null
    }
}