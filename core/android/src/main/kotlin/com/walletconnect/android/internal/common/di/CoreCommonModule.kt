package com.walletconnect.android.internal.common.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.tinder.scarlet.utils.getRawType
import com.walletconnect.android.internal.common.JsonRpcResponse
import com.walletconnect.android.internal.common.adapter.ExpiryAdapter
import com.walletconnect.android.internal.common.adapter.JsonRpcResultAdapter
import com.walletconnect.android.internal.common.adapter.TagsAdapter
import com.walletconnect.android.internal.common.model.Expiry
import com.walletconnect.android.internal.common.model.Tags
import com.walletconnect.android.pulse.model.EventType
import com.walletconnect.android.pulse.model.properties.Props
import com.walletconnect.foundation.di.FoundationDITags
import com.walletconnect.foundation.di.foundationCommonModule
import com.walletconnect.foundation.util.Logger
import org.koin.core.qualifier.named
import org.koin.dsl.module
import timber.log.Timber
import kotlin.reflect.jvm.jvmName

fun coreCommonModule() = module {

	includes(foundationCommonModule())

	single<PolymorphicJsonAdapterFactory<JsonRpcResponse>> {
		PolymorphicJsonAdapterFactory.of(JsonRpcResponse::class.java, "type")
			.withSubtype(JsonRpcResponse.JsonRpcResult::class.java, "result")
			.withSubtype(JsonRpcResponse.JsonRpcError::class.java, "error")
	}

	single<PolymorphicJsonAdapterFactory<Props>> {
		PolymorphicJsonAdapterFactory.of(Props::class.java, "type")
			.withSubtype(Props.W3M.ModalCreated::class.java, EventType.W3M.MODAL_CREATED)
			.withSubtype(Props.W3M.ModalLoaded::class.java, EventType.W3M.MODAL_LOADED)
			.withSubtype(Props.W3M.ModalOpen::class.java, EventType.W3M.MODAL_OPEN)
			.withSubtype(Props.W3M.ModalClose::class.java, EventType.W3M.MODAL_CLOSE)
			.withSubtype(Props.W3M.ClickNetworks::class.java, EventType.W3M.CLICK_NETWORKS)
			.withSubtype(Props.W3M.ClickAllWallets::class.java, EventType.W3M.CLICK_ALL_WALLETS)
			.withSubtype(Props.W3M.SwitchNetwork::class.java, EventType.W3M.SWITCH_NETWORK)
			.withSubtype(Props.W3M.SelectWallet::class.java, EventType.W3M.SELECT_WALLET)
			.withSubtype(Props.W3M.ConnectSuccess::class.java, EventType.W3M.CONNECT_SUCCESS)
			.withSubtype(Props.W3M.ConnectError::class.java, EventType.W3M.CONNECT_ERROR)
			.withSubtype(Props.W3M.DisconnectSuccess::class.java, EventType.W3M.DISCONNECT_SUCCESS)
			.withSubtype(Props.W3M.DisconnectError::class.java, EventType.W3M.DISCONNECT_ERROR)
			.withSubtype(Props.W3M.ClickWalletHelp::class.java, EventType.W3M.CLICK_WALLET_HELP)
			.withSubtype(Props.W3M.ClickNetworkHelp::class.java, EventType.W3M.CLICK_NETWORK_HELP)
			.withSubtype(Props.W3M.ClickGetWallet::class.java, EventType.W3M.CLICK_GET_WALLET)
			.withSubtype(Props.TraceProps.Pairing.MalformedPairingUri::class.java, EventType.Pairing.MALFORMED_PAIRING_URI)
			.withSubtype(Props.TraceProps.Pairing.PairingAlreadyExist::class.java, EventType.Pairing.PAIRING_ALREADY_EXIST)
			.withSubtype(Props.TraceProps.Pairing.PairingSubscriptionFailure::class.java, EventType.Pairing.PAIRING_SUBSCRIPTION_FAILURE)
			.withSubtype(Props.TraceProps.Pairing.PairingExpired::class.java, EventType.Pairing.PAIRING_EXPIRED)
			.withSubtype(Props.TraceProps.NoWSSConnection::class.java, EventType.NO_WSS_CONNECTION)
			.withSubtype(Props.TraceProps.NoInternetConnection::class.java, EventType.NO_INTERNET_CONNECTION)
			.withSubtype(Props.TraceProps.Session.ProposalExpired::class.java, EventType.Session.PROPOSAL_EXPIRED)
			.withSubtype(Props.TraceProps.Session.SessionSettlePublishFailure::class.java, EventType.Session.SESSION_SETTLE_PUBLISH_FAILURE)
			.withSubtype(Props.TraceProps.Session.SessionApprovePublishFailure::class.java, EventType.Session.SESSION_APPROVE_PUBLISH_FAILURE)
			.withSubtype(Props.TraceProps.Session.SessionSubscriptionFailure::class.java, EventType.Session.SESSION_SUBSCRIPTION_FAILURE)
			.withSubtype(Props.TraceProps.Session.SessionApproveNamespaceValidationFailure::class.java, EventType.Session.SESSION_APPROVE_NAMESPACE_VALIDATION_FAILURE)
			.withSubtype(Props.TraceProps.Session.RequiredNamespaceValidationFailure::class.java, EventType.Session.REQUIRED_NAMESPACE_VALIDATION_FAILURE)
			.withSubtype(Props.TraceProps.Session.OptionalNamespaceValidationFailure::class.java, EventType.Session.OPTIONAL_NAMESPACE_VALIDATION_FAILURE)
			.withSubtype(Props.TraceProps.Session.SessionPropertiesValidationFailure::class.java, EventType.Session.SESSION_PROPERTIES_VALIDATION_FAILURE)
			.withSubtype(
				Props.TraceProps.SessionAuthenticate.AuthenticatedSessionApprovePublishFailure::class.java,
				EventType.SessionAuthenticate.AUTHENTICATED_SESSION_APPROVE_PUBLISH_FAILURE
			)
			.withSubtype(Props.TraceProps.SessionAuthenticate.MissingSessionAuthenticateRequest::class.java, EventType.SessionAuthenticate.MISSING_SESSION_AUTH_REQUEST)
			.withSubtype(Props.TraceProps.SessionAuthenticate.SessionAuthenticateRequestExpired::class.java, EventType.SessionAuthenticate.SESSION_AUTH_REQUEST_EXPIRED)
			.withSubtype(Props.TraceProps.SessionAuthenticate.InvalidCacao::class.java, EventType.SessionAuthenticate.INVALID_CACAO)
			.withSubtype(Props.TraceProps.SessionAuthenticate.ChainsCaip2CompliantFailure::class.java, EventType.SessionAuthenticate.CHAINS_CAIP2_COMPLIANT_FAILURE)
			.withSubtype(Props.TraceProps.SessionAuthenticate.ChainsEvmCompliantFailure::class.java, EventType.SessionAuthenticate.CHAINS_EVM_COMPLIANT_FAILURE)
			.withSubtype(
				Props.TraceProps.SessionAuthenticate.SubscribeAuthenticatedSessionTopicFailure::class.java,
				EventType.SessionAuthenticate.SUBSCRIBE_AUTH_SESSION_TOPIC_FAILURE
			)
			.withSubtype(Props.TraceProps.SessionAuthenticate.AuthenticatedSessionExpired::class.java, EventType.SessionAuthenticate.AUTHENTICATED_SESSION_EXPIRED)
	}

	single<Moshi.Builder>(named(AndroidCommonDITags.MOSHI)) {
		get<Moshi>(named(FoundationDITags.MOSHI))
			.newBuilder()
			.add { type, _, moshi ->
				when (type.getRawType().name) {
					Expiry::class.jvmName -> ExpiryAdapter
					Tags::class.jvmName -> TagsAdapter
					JsonRpcResponse.JsonRpcResult::class.jvmName -> JsonRpcResultAdapter(moshi)
					else -> null
				}
			}
			.add(get<PolymorphicJsonAdapterFactory<JsonRpcResponse>>())
			.add(get<PolymorphicJsonAdapterFactory<Props>>())
	}

	single {
		Timber
	}

	single<Logger>(named(AndroidCommonDITags.LOGGER)) {
		object : Logger {
			override fun log(logMsg: String?) {
				get<Timber.Forest>().d(logMsg)
			}

			override fun log(throwable: Throwable?) {
				get<Timber.Forest>().d(throwable)
			}

			override fun error(errorMsg: String?) {
				get<Timber.Forest>().e(errorMsg)
			}

			override fun error(throwable: Throwable?) {
				get<Timber.Forest>().e(throwable)
			}
		}
	}
}