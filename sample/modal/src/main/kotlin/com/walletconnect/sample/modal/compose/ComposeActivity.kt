@file:OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalMaterialApi::class)

package com.walletconnect.sample.modal.compose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.material.*
import com.walletconnect.sample.modal.ui.theme.WalletConnectTheme
import com.walletconnect.web3.modal.ui.components.internal.Web3ModalComponent
import kotlinx.coroutines.launch

class ComposeActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WalletConnectTheme {
                val scaffoldState: ScaffoldState = rememberScaffoldState()
                val sheetState = rememberModalBottomSheetState(
                    initialValue = ModalBottomSheetValue.Hidden,
                    skipHalfExpanded = true
                )
                val bottomSheetNavigator = BottomSheetNavigator(sheetState)
                val navController = rememberNavController(bottomSheetNavigator)
                val coroutineScope = rememberCoroutineScope()
                var showModal by remember { mutableStateOf(false) }
                var count by remember { mutableStateOf(1) }
//                val modalSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden, skipHalfExpanded = true)
                Scaffold(
                    scaffoldState = scaffoldState,
                    topBar = {
                        TopAppBar(
                            title = { Text(text = "Jetpack Compose") },
                            navigationIcon = {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    null,
                                    modifier = Modifier.clickable { this.finish() })
                            }
                        )
                    },
                ) { paddingValues ->
                    if (showModal) {
                        DisposableEffect(Unit) {
                            onDispose {
                                Log.e("ComposeActivity", "Modal is disposed")
                                showModal = false
                            }
                        }
                    }
                    Log.e("ComposeActivity", "$showModal")
                    val so = LocalViewModelStoreOwner.current

                    BottomSheetContent(
                        count = count,
                        showModal = showModal,
                        sheetState = sheetState,
                        onClose = {
                            coroutineScope.launch {
                                sheetState.hide()
                                showModal = false
                                Log.e("ComposeActivity", "$showModal")
                               // so!!.viewModelStore.clear()
                            }
                        },
                        modifier = Modifier.padding(paddingValues)
                    )

                    Button(onClick = {
                        showModal = true
                        count++
                        coroutineScope.launch { sheetState.show() }
                    }, modifier = Modifier.padding(paddingValues)) {
                        Text("Open Bottom Sheet")
                    }
                }
            }
        }



    }
}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetContent(
    count:Int,
    showModal: Boolean,
    sheetState: ModalBottomSheetState,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Log.e("ComposeActivity", "BottomSheetContent")

    if (showModal && count > 0) {
        CustomLifecycleOwnerProvider {
            ModalBottomSheetLayout(
                sheetState = sheetState,
                sheetContent = {
                    Web3ModalComponent(
                        shouldOpenChooseNetwork = false,
                        closeModal = onClose
                    )
                },
                modifier = modifier
            ) {}
        }
    }
}

class CustomLifecycleOwner : LifecycleOwner {
    private val lifecycleRegistry = LifecycleRegistry(this)

    init {
        lifecycleRegistry.currentState = Lifecycle.State.RESUMED
    }

    override val lifecycle: Lifecycle
        get() = lifecycleRegistry
}

@Composable
fun CustomLifecycleOwnerProvider(content: @Composable () -> Unit) {
    val lifecycleOwner =  CustomLifecycleOwner()

    val viewModelStoreOwner =
        object : ViewModelStoreOwner {
            override val viewModelStore = ViewModelStore()
        }
    

    CompositionLocalProvider(
        LocalViewModelStoreOwner provides viewModelStoreOwner,
        LocalLifecycleOwner provides lifecycleOwner,
        content = content
    )
}
