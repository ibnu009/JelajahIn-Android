package com.ibnu.jelajahin.core.ui.adapter.handler

interface BookmarkItemHandler {
    fun onItemClicked(uuid: String, type: String)
    fun onUnBookmarkClick(uuid: String)
}