package com.walletconnect.sample.wallet

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager

import com.mixpanel.android.mpmetrics.MixpanelAPI
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.common.configuration.Placement
import com.pandulapeter.beagle.modules.DividerModule
import com.pandulapeter.beagle.modules.HeaderModule
import com.pandulapeter.beagle.modules.PaddingModule
import com.pandulapeter.beagle.modules.TextInputModule
import com.pandulapeter.beagle.modules.TextModule
import com.walletconnect.android.Core
import com.walletconnect.android.CoreClient
import com.walletconnect.android.cacao.signature.SignatureType
import com.walletconnect.android.internal.common.di.AndroidCommonDITags
import com.walletconnect.android.internal.common.wcKoinApp
import com.walletconnect.android.utils.cacao.sign
import com.walletconnect.foundation.util.Logger
import com.walletconnect.notify.client.Notify
import com.walletconnect.notify.client.NotifyClient
import com.walletconnect.notify.client.cacao.CacaoSigner
import com.walletconnect.sample.common.initBeagle
import com.walletconnect.sample.common.tag
import com.walletconnect.sample.wallet.domain.EthAccountDelegate
import com.walletconnect.sample.wallet.domain.NotificationHandler
import com.walletconnect.sample.wallet.domain.NotifyDelegate
import com.walletconnect.sample.wallet.domain.mixPanel
import com.walletconnect.sample.wallet.ui.state.ConnectionState
import com.walletconnect.sample.wallet.ui.state.connectionStateFlow
import com.walletconnect.util.hexToBytes
import com.walletconnect.web3.wallet.client.Wallet
import com.walletconnect.web3.wallet.client.Web3Wallet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.core.qualifier.named
import timber.log.Timber
import com.walletconnect.sample.common.BuildConfig as CommonBuildConfig

class Web3WalletApplication : Application() {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var addFirebaseBeagleModules: () -> Unit = {}
    private lateinit var logger: Logger

    override fun onCreate() {
        super.onCreate()

        EthAccountDelegate.application = this
https://www.lightpollutionmap.info/geoserver/gwc/service/wms?SERVICE=WMS&VERSION=1.3.0&REQUEST=GetMap&FORMAT=image%2Fpng&TRANSPARENT=true&LAYERS=PostGIS%3AWA_2015&TILED=true&STYLES=WA&SRS=EPSG%3A3857&WIDTH=256&HEIGHT=256&CRS=EPSG%3A3857&BBOX=3130872.4899728%2C7514093.975934721%2C3443959.73897008%2C7827181.224932001
        val projectId = BuildConfig.PROJECT_ID
        val appMetaData = Core.Model.AppMetaData(
            name = "Kotlin Wallet",
            description = "Kotlin Wallet Implementation",
            url = "https://web3modal-laboratory-git-chore-kotlin-assetlinks-walletconnect1.vercel.app",
            icons = listOf("https://raw.githubusercontent.com/WalletConnect/walletconnect-assets/master/Icon/Gradient/Icon.png"),
            redirect = "kotlin-web3wallet://request",
            appLink = BuildConfig.WALLET_APP_LINK,
            linkMode = true
        )

        CoreClient.initialize(
            application = this,
            projectId = projectId,
            metaData = appMetaData,
            onError = { error ->
//                Firebase.crashlytics.recordException(error.throwable)
                println(error.throwable.stackTraceToString())
                scope.launch {
                    connectionStateFlow.emit(ConnectionState.Error(error.throwable.message ?: ""))
                }
            }
        )

        mixPanel = MixpanelAPI.getInstance(this, CommonBuildConfig.MIX_PANEL, true).apply {
            identify(CoreClient.Push.clientId)
            people.set("\$name", EthAccountDelegate.ethAddress)
        }

        logger = wcKoinApp.koin.get(named(AndroidCommonDITags.LOGGER))
        logger.log("Account: ${EthAccountDelegate.account}")

        Web3Wallet.initialize(Wallet.Params.Init(core = CoreClient),
            onSuccess = {
                logger.log("Web3Wallet initialized")
            },
            onError = { error ->
              //  Firebase.crashlytics.recordException(error.throwable)
                logger.error(error.throwable.stackTraceToString())
            })

        NotifyClient.initialize(
            init = Notify.Params.Init(CoreClient)
        ) { error ->
           // Firebase.crashlytics.recordException(error.throwable)
            logger.error(error.throwable.stackTraceToString())
        }



        registerAccount()
        initializeBeagle()



        wcKoinApp.koin.get<Timber.Forest>().plant(object : Timber.Tree() {
            override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                if (t != null) {
                    mixPanel.track("error: $t, message: $message")
                } else {
                    mixPanel.track(message)
                }
            }
        })



        handleNotifyMessages()
    }

    private fun initializeBeagle() {
        initBeagle(
            this@Web3WalletApplication,
            HeaderModule(
                title = getString(R.string.app_name),
                subtitle = BuildConfig.APPLICATION_ID,
                text = "${BuildConfig.BUILD_TYPE} "
            ),
            DividerModule(),
            TextModule(text = EthAccountDelegate.ethAddress) {
                (getSystemService(CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(ClipData.newPlainText("Account", EthAccountDelegate.ethAddress))
            },
            PaddingModule(size = PaddingModule.Size.LARGE),
            TextModule(text = EthAccountDelegate.privateKey) {
                (getSystemService(CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(ClipData.newPlainText("Private Key", EthAccountDelegate.privateKey))
            },
            PaddingModule(size = PaddingModule.Size.LARGE),
            TextModule(text = CoreClient.Push.clientId, id = CoreClient.Push.clientId) {
                (getSystemService(CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(ClipData.newPlainText("ClientId", CoreClient.Push.clientId))
            },
            DividerModule(),
            TextInputModule(
                text = "Import Private Key",
                areRealTimeUpdatesEnabled = false,
                validator = { text ->
                    !text.startsWith("0x") && text.length == 64
                },
                onValueChanged = { text ->
                    NotifyClient.unregister(
                        params = Notify.Params.Unregister(
                            EthAccountDelegate.ethAddress,
                        ),
                        onSuccess = {
                            logger.log("Unregister Success")
                            EthAccountDelegate.privateKey = text
                            registerAccount()
                        },
                        onError = { logger.error(it.throwable.stackTraceToString()) }
                    )
                }
            ),
        )
        addFirebaseBeagleModules()
    }


    private fun handleNotifyMessages() {
        val scope = CoroutineScope(Dispatchers.Default)

        val notifyEventsJob = NotifyDelegate.notifyEvents
            .filterIsInstance<Notify.Event.Notification>()
            .onEach { notification -> NotificationHandler.addNotification(notification.notification) }
            .launchIn(scope)


        val notificationDisplayingJob = NotificationHandler.startNotificationDisplayingJob(scope, this)


        notifyEventsJob.invokeOnCompletion { cause ->
            onScopeCancelled(cause, "notifyEventsJob")
        }

        notificationDisplayingJob.invokeOnCompletion { cause ->
            onScopeCancelled(cause, "notificationDisplayingJob")
        }
    }

    private fun onScopeCancelled(error: Throwable?, job: String) {
        wcKoinApp.koin.get<Logger>(named(AndroidCommonDITags.LOGGER)).error("onScopeCancelled($job): $error")
    }

    private fun registerAccount() {
        val account = EthAccountDelegate.ethAddress
        val domain = BuildConfig.APPLICATION_ID
        val isRegistered = NotifyClient.isRegistered(params = Notify.Params.IsRegistered(account = account, domain = domain))

        if (!isRegistered) {
            NotifyClient.prepareRegistration(
                params = Notify.Params.PrepareRegistration(account = account, domain = domain),
                onSuccess = { cacaoPayloadWithIdentityPrivateKey, message ->
                    logger.log("PrepareRegistration Success: $cacaoPayloadWithIdentityPrivateKey")

                    val signature = CacaoSigner.sign(message, EthAccountDelegate.privateKey.hexToBytes(), SignatureType.EIP191)

                    NotifyClient.register(
                        params = Notify.Params.Register(cacaoPayloadWithIdentityPrivateKey = cacaoPayloadWithIdentityPrivateKey, signature = signature),
                        onSuccess = { logger.log("Register Success") },
                        onError = { logger.error(it.throwable.stackTraceToString()) }
                    )

                },
                onError = { logger.error(it.throwable.stackTraceToString()) }
            )
        } else {
            logger.log("$account is already registered")
        }
    }

}
