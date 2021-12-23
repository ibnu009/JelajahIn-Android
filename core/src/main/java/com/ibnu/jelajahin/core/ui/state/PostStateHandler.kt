package com.ibnu.jelajahin.core.ui.state

interface PostStateHandler {
    fun onInitiating()
    fun onSuccess(message: String)
    fun onFailure(message: String)
}