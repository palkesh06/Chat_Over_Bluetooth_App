package com.example.chatoverbluetooth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatoverbluetooth.domain.chat.BTController
import com.example.chatoverbluetooth.domain.chat.BTDevice
import com.example.chatoverbluetooth.domain.chat.ConnectionResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BTViewModel @Inject constructor(
    private val btController: BTController
) : ViewModel() {
    private val _state = MutableStateFlow(BTUiState())
    val state = combine(
        btController.scannedDevices,
        btController.pairedDevices,
        _state
    ) { scannedDevices, pairedDevices, state ->
        state.copy(
            scannedDevices = scannedDevices,
            pairedDevices = pairedDevices,
            messages = if (state.isConnected) state.messages else emptyList()
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        _state.value
    )

    private var deviceConnectionJob: Job? = null

    init {
        btController.isConnected.onEach { isConnected ->
            _state.update { it.copy(isConnected = isConnected) }
        }.launchIn(viewModelScope)

        btController.errors.onEach { errMsg ->
            _state.update { it.copy(errorMsg = errMsg) }
        }.launchIn(viewModelScope)
    }

    fun startScan() {
        btController.startDiscovery()
    }

    fun stopScan() {
        btController.stopDiscovery()
    }

    fun connectToDevice(device: BTDevice) {
        _state.update { it.copy(isConnecting = true) }
        deviceConnectionJob = btController.connectToDevice(device)
            .listen()
    }

    fun disconnectFromDevice() {
        deviceConnectionJob?.cancel()
        btController.closeConnection()
        _state.update {
            it.copy(
                isConnecting = false,
                isConnected = false
            )
        }
    }

    fun waitForIncomingConnection() {
        _state.update { it.copy(isConnecting = true) }
        deviceConnectionJob = btController
            .startBTServer()
            .listen()
    }

    fun sendMsg(msg: String) {
        viewModelScope.launch {
            val btMsg = btController.trySendMsg(msg)
            if (btMsg != null)
                _state.update {
                    it.copy(
                        messages = it.messages + btMsg
                    )
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        btController.release()
    }

    private fun Flow<ConnectionResult>.listen(): Job {
        return onEach { result ->
            when (result) {
                ConnectionResult.ConnectionEstablished -> {
                    _state.update {
                        it.copy(
                            isConnected = true,
                            isConnecting = false,
                            errorMsg = null
                        )
                    }
                }

                is ConnectionResult.ConnectionError -> {
                    _state.update {
                        it.copy(
                            isConnected = false,
                            isConnecting = false,
                            errorMsg = result.msg
                        )
                    }
                }

                is ConnectionResult.TransferSucceeded -> {
                    _state.update {
                        it.copy(
                            messages = it.messages + result.msg
                        )
                    }
                }
            }
        }.catch { throwable ->
            _state.update {
                it.copy(
                    isConnected = false,
                    isConnecting = false,
                )
            }
        }.launchIn(viewModelScope)
    }
}