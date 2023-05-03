package com.walletconnect.sample.dapp.ui.routes.composable_routes.account

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.walletconnect.sample.dapp.domain.DappDelegate
import com.walletconnect.sample.dapp.ui.DappSampleEvents
import com.walletconnect.sample.dapp.ui.accountArg
import com.walletconnect.sample_common.*
import com.walletconnect.sign.client.Sign
import com.walletconnect.sign.client.SignClient
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AccountViewModel(
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val selectedAccountInfo = checkNotNull(savedStateHandle.get<String>(accountArg))

    private val _uiState: MutableStateFlow<AccountUi> = MutableStateFlow(AccountUi.Loading)
    val uiState: StateFlow<AccountUi> = _uiState.asStateFlow()

    private val _events: MutableSharedFlow<DappSampleEvents> = MutableSharedFlow()
    val events: SharedFlow<DappSampleEvents>
        get() = _events.asSharedFlow()

    init {
        DappDelegate.wcEventModels
            .filterNotNull()
            .onEach { walletEvent ->
                when (walletEvent) {
                    is Sign.Model.UpdatedSession -> { fetchAccountDetails() }
                    is Sign.Model.SessionRequestResponse -> {
                        val request = when (walletEvent.result) {
                            is Sign.Model.JsonRpcResponse.JsonRpcResult -> {
                                val successResult =
                                    (walletEvent.result as Sign.Model.JsonRpcResponse.JsonRpcResult)
                                DappSampleEvents.RequestSuccess(successResult.result)
                            }
                            is Sign.Model.JsonRpcResponse.JsonRpcError -> {
                                val errorResult =
                                    (walletEvent.result as Sign.Model.JsonRpcResponse.JsonRpcError)
                                DappSampleEvents.RequestPeerError("Error Message: ${errorResult.message}\n Error Code: ${errorResult.code}")
                            }
                        }

                        _events.emit(request)
                    }
                    is Sign.Model.DeletedSession -> {
                        _events.emit(DappSampleEvents.Disconnect)
                    }
                    else -> Unit
                }
            }
            .launchIn(viewModelScope)
    }

    fun requestMethod(method: String, sendSessionRequestDeepLink: (Uri) -> Unit) {
        (uiState.value as? AccountUi.AccountData)?.let { currentState ->
            val (parentChain, chainId, account) = currentState.selectedAccount.split(":")
            val params: String = when {
                method.equals("personal_sign", true) -> getPersonalSignBody(account)
                method.equals("eth_sign", true) -> getEthSignBody(account)
                method.equals("eth_sendTransaction", true) -> getEthSendTransaction(account)
                method.equals("eth_signTypedData", true) -> getEthSignTypedData(account)
                else -> "[]"
            }
            val requestParams = Sign.Params.Request(
                sessionTopic = requireNotNull(DappDelegate.selectedSessionTopic),
                method = method,
                params = params, // stringified JSON
                chainId = "$parentChain:$chainId"
            )

            SignClient.request(requestParams) {
                viewModelScope.launch {
                    _events.emit(
                        DappSampleEvents.RequestError(
                            it.throwable.localizedMessage ?: "Error trying to send request"
                        )
                    )
                }
            }

            SignClient.getActiveSessionByTopic(requestParams.sessionTopic)?.redirect?.toUri()
                ?.let { deepLinkUri -> sendSessionRequestDeepLink(deepLinkUri) }
        }
    }

    fun fetchAccountDetails() {
        val (chainNamespace, chainReference, account) = selectedAccountInfo.split(":")
        val chainDetails = Chains.values().first {
            it.chainNamespace == chainNamespace && it.chainReference == chainReference
        }

        val listOfMethodsByChainId: List<String> =
            SignClient.getListOfActiveSessions()
                .filter { session -> session.topic == DappDelegate.selectedSessionTopic }
                .flatMap { session ->
                    session.namespaces
                        .filter { (namespaceKey, _) -> namespaceKey == chainDetails.chainId }
                        .flatMap { (_, namespace) -> namespace.methods }
                }

        val listOfMethodsByNamespace: List<String> =
            SignClient.getListOfActiveSessions()
                .filter { session -> session.topic == DappDelegate.selectedSessionTopic }
                .flatMap { session ->
                    session.namespaces
                        .filter { (namespaceKey, _) -> namespaceKey == chainDetails.chainNamespace }
                        .flatMap { (_, namespace) -> namespace.methods }
                }


        viewModelScope.launch {
            _uiState.value = AccountUi.AccountData(
                icon = chainDetails.icon,
                chainName = chainDetails.chainName,
                account = account,
                listOfMethods = listOfMethodsByChainId.ifEmpty { listOfMethodsByNamespace },
                selectedAccount = selectedAccountInfo
            )
        }
    }
}