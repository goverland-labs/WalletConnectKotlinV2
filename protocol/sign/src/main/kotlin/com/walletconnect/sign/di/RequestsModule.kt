package com.walletconnect.sign.di

import com.walletconnect.android.internal.common.di.AndroidCommonDITags
import com.walletconnect.sign.engine.use_case.requests.OnPingUseCase
import com.walletconnect.sign.engine.use_case.requests.OnSessionDeleteUseCase
import com.walletconnect.sign.engine.use_case.requests.OnSessionEventUseCase
import com.walletconnect.sign.engine.use_case.requests.OnSessionExtendUseCase
import com.walletconnect.sign.engine.use_case.requests.OnSessionProposalUseCase
import com.walletconnect.sign.engine.use_case.requests.OnSessionRequestUseCase
import com.walletconnect.sign.engine.use_case.requests.OnSessionSettleUseCase
import com.walletconnect.sign.engine.use_case.requests.OnSessionUpdateUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module

@JvmSynthetic
internal fun requestsModule() = module {

    single {
        OnSessionProposalUseCase(
            pairingController = get(),
            jsonRpcInteractor = get(),
            proposalStorageRepository = get(),
            resolveAttestationIdUseCase = get(),
            logger = get(named(AndroidCommonDITags.LOGGER))
        )
    }

    single {
        OnSessionSettleUseCase(
            proposalStorageRepository = get(),
            jsonRpcInteractor = get(),
            pairingController = get(),
            metadataStorageRepository = get(),
            sessionStorageRepository = get(),
            crypto = get(),
            selfAppMetaData = get(),
            logger = get(named(AndroidCommonDITags.LOGGER))
        )
    }

    single {
        OnSessionRequestUseCase(
            metadataStorageRepository = get(),
            sessionStorageRepository = get(),
            jsonRpcInteractor = get(),
            resolveAttestationIdUseCase = get(),
            logger = get(named(AndroidCommonDITags.LOGGER)),
            enableRequestsQueue = get(named(SignDITags.ENABLE_REQUESTS_QUEUE))
        )
    }

    single { OnSessionDeleteUseCase(jsonRpcInteractor = get(), crypto = get(), sessionStorageRepository = get(), logger = get(named(AndroidCommonDITags.LOGGER))) }

    single { OnSessionEventUseCase(jsonRpcInteractor = get(), sessionStorageRepository = get(), logger = get(named(AndroidCommonDITags.LOGGER))) }

    single { OnSessionUpdateUseCase(jsonRpcInteractor = get(), sessionStorageRepository = get(), logger = get(named(AndroidCommonDITags.LOGGER))) }

    single { OnSessionExtendUseCase(jsonRpcInteractor = get(), sessionStorageRepository = get(), logger = get(named(AndroidCommonDITags.LOGGER))) }

    single { OnPingUseCase(jsonRpcInteractor = get(), logger = get(named(AndroidCommonDITags.LOGGER))) }
}