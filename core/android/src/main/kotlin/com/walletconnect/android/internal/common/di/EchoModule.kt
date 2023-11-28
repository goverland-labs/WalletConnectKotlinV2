package com.walletconnect.android.internal.common.di

import android.content.SharedPreferences
import com.walletconnect.android.echo.client.EchoInterface
import com.walletconnect.android.echo.network.EchoService
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@JvmSynthetic
internal fun echoModule() = module {

    factory(named(AndroidCommonDITags.ECHO_URL)) { ECHO_URL }

    single(named(AndroidCommonDITags.ECHO_RETROFIT)) {
        Retrofit.Builder()
            .baseUrl(get<String>(named(AndroidCommonDITags.ECHO_URL)))
            .addConverterFactory(MoshiConverterFactory.create())
            .client(get(named(AndroidCommonDITags.OK_HTTP)))
            .build()
    }

    single {
        get<Retrofit>(named(AndroidCommonDITags.ECHO_RETROFIT)).create(EchoService::class.java)
    }

    single(named(AndroidCommonDITags.CLIENT_ID)) {
        requireNotNull(get<SharedPreferences>().getString(EchoInterface.KEY_CLIENT_ID, null))
    }
}