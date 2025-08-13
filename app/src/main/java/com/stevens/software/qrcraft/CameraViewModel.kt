package com.stevens.software.qrcraft

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class CameraViewModel: ViewModel() {
    private val _snackBar: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val snackBar = _snackBar.asSharedFlow()

    fun showPermissionGrantedSnackBar(){
        viewModelScope.launch {
            _snackBar.emit(true)
        }

    }
}