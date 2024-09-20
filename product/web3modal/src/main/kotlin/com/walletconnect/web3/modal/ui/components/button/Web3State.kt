package com.walletconnect.web3.modal.ui.components.button

import com.walletconnect.android.internal.common.wcKoinApp
import com.walletconnect.foundation.util.Logger
import com.walletconnect.web3.modal.client.*
import com.walletconnect.web3.modal.client.models.request.*
import com.walletconnect.web3.modal.domain.usecase.*
import com.walletconnect.web3.modal.engine.Web3ModalEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

class Web3State(
    coroutineScope: CoroutineScope,
) {

    private val observeSessionTopicUseCase: ObserveSessionUseCase = wcKoinApp.koin.get()
    private val getSessionUseCase: GetSessionUseCase = wcKoinApp.koin.get()
    private val sessionTopicFlow = observeSessionTopicUseCase()
    val isConnected = sessionTopicFlow
        .map { it != null && getSessionUseCase() != null }
        .map { Web3Modal.getAccount() != null }
        .shareIn(coroutineScope, started = SharingStarted.Eagerly, 1)

    companion object {

        fun sendSIWEOverPersonalSign(
            params: Modal.Model.AuthPayloadParams,
            onSuccess: (SentRequestResult) -> Unit,
            onError: (Throwable) -> Unit
        ) {
            val web3ModalEngine: Web3ModalEngine = wcKoinApp.koin.get()
            val logger: Logger = wcKoinApp.koin.get()

            web3ModalEngine.shouldDisconnect = false
            val account = web3ModalEngine.getAccount() ?: throw IllegalStateException("Account is null")
            val issuer = "did:pkh:${account.chain.id}:${account.address}"
            val siweMessage = web3ModalEngine.formatSIWEMessage(params, issuer)
            val msg = siweMessage.encodeToByteArray().joinToString(separator = "", prefix = "0x") { eachByte -> "%02x".format(eachByte) }
            val body = "[\"$msg\", \"${account.address}\"]"
            web3ModalEngine.request(
                request = Request("personal_sign", body),
                onSuccess = { sendRequest ->
                    logger.log("SIWE sent successfully")
                    web3ModalEngine.siweRequestIdWithMessage = Pair((sendRequest as SentRequestResult.WalletConnect).requestId, siweMessage)
                    onSuccess(sendRequest)
                },
                onError = {
                    if (it !is Web3ModalEngine.RedirectMissingThrowable) {
                        web3ModalEngine.shouldDisconnect = true
                    }

                    onError(it)
                },
            )
        }
    }
}
