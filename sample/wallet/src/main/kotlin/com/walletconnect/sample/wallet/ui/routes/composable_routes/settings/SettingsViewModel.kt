package com.walletconnect.sample.wallet.ui.routes.composable_routes.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.walletconnect.android.CoreClient
import com.walletconnect.sample.wallet.domain.EthAccountDelegate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {
    val caip10 = EthAccountDelegate.ethAddress
    val privateKey = EthAccountDelegate.privateKey
    val clientId = CoreClient.Echo.clientId

    private val _deviceToken = MutableStateFlow("")
    val deviceToken = _deviceToken.asStateFlow()

    init {
        viewModelScope.launch {

        }
    }
}
