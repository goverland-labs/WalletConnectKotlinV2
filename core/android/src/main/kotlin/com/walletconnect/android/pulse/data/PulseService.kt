@file:JvmSynthetic

package com.walletconnect.android.pulse.data

import com.walletconnect.android.pulse.model.Event
import com.walletconnect.android.pulse.model.properties.Props
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface PulseService {
    @Headers("Content-Type: application/json")
    @POST("/e")
    suspend fun sendEvent(@Header("x-sdk-type") sdkType: String,  @Body body: Event<Props>): Response<Unit>
}