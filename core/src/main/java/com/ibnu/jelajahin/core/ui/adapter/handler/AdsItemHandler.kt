package com.ibnu.jelajahin.core.ui.adapter.handler

interface AdsItemHandler {
    fun onBannerClicked(
        actionType: String,
        actionValue: String?,
        actionParam: String?
    )
}