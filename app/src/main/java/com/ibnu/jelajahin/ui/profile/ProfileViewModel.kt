package com.ibnu.jelajahin.ui.profile

import androidx.lifecycle.ViewModel
import com.ibnu.jelajahin.core.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

}