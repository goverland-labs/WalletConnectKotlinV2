package com.walletconnect.sample.modal.compose

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.walletconnect.web3.modal.client.Web3Modal
import com.walletconnect.web3.modal.ui.components.button.AccountButton
import com.walletconnect.web3.modal.ui.components.button.AccountButtonType
import com.walletconnect.web3.modal.ui.components.button.ConnectButton
import com.walletconnect.web3.modal.ui.components.button.ConnectButtonSize
import com.walletconnect.web3.modal.ui.components.button.NetworkButton
import com.walletconnect.web3.modal.ui.components.button.Web3Button
import com.walletconnect.web3.modal.ui.components.button.rememberWeb3ModalState
import com.walletconnect.web3.modal.ui.components.internal.Web3ModalComponent

@Composable
fun HomeScreen(navController: NavController) {
    val web3ModalState = rememberWeb3ModalState(navController = navController)
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(Modifier.height(500.dp)) {
            Web3ModalComponent(false, { } )

        }

        ConnectButton(state = web3ModalState, buttonSize = ConnectButtonSize.NORMAL)
        Spacer(modifier = Modifier.height(20.dp))
        ConnectButton(state = web3ModalState, buttonSize = ConnectButtonSize.SMALL)
        Spacer(modifier = Modifier.height(20.dp))
        AccountButton(web3ModalState, AccountButtonType.NORMAL)
        Spacer(modifier = Modifier.height(20.dp))
        AccountButton(web3ModalState, AccountButtonType.MIXED)
        Spacer(modifier = Modifier.height(20.dp))
        Web3Button(state = web3ModalState)
        Spacer(modifier = Modifier.height(20.dp))
        NetworkButton(state = web3ModalState)
    }
}
