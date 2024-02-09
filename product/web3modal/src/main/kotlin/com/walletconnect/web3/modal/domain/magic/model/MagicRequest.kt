package com.walletconnect.web3.modal.domain.magic.model

import com.walletconnect.android.internal.common.model.AppMetaData

internal abstract class MagicRequest(val type: MagicTypeRequest) {
    override fun toString(): String = "{type: \'${type.value}\'}"
}

internal object IsConnected : MagicRequest(MagicTypeRequest.IS_CONNECTED)

internal data class SwitchNetwork(val chainId: String) : MagicRequest(MagicTypeRequest.SWITCH_NETWORK) {
    override fun toString(): String = "{type: \'${super.type.value}\',payload:{chainId:$chainId}"
}

internal data class ConnectEmail(val email: String) : MagicRequest(MagicTypeRequest.CONNECT_EMAIL) {
    override fun toString(): String = "{type: \'${super.type.value}\',payload:{email:$email}"
}

internal object ConnectDevice : MagicRequest(MagicTypeRequest.CONNECT_DEVICE)

internal data class ConnectOtp(val otp: String) : MagicRequest(MagicTypeRequest.CONNECT_OTP) {
    override fun toString(): String = "{type: \'${super.type.value}\',payload:{otp:$otp}"
}

internal data class GetUser(val chainId: String?) : MagicRequest(MagicTypeRequest.GET_USER) {
    override fun toString(): String {
        if (chainId?.isNotEmpty() == false) {
            return "{type:\'${super.type}\',payload:{chainId:$chainId}}";
        }
        return "{type:\'${super.type}\'}";
    }
}

internal object GetChainId : MagicRequest(MagicTypeRequest.GET_CHAIN_ID)

internal object SignOut : MagicRequest(MagicTypeRequest.SIGN_OUT)

internal data class RpcRequest(val method: String, val params: String) : MagicRequest(MagicTypeRequest.RPC_REQUEST) {
    override fun toString(): String {
        val m = "'method:\'$method\'"
        return "{type:\'${super.type}\',payload:{$m,params:[$params]}}"
    }
}

internal object UpdateEmail : MagicRequest(MagicTypeRequest.UPDATE_EMAIL)

internal data class SyncDappData(val metaData: AppMetaData, val sdkVersion: String, val projectId: String) : MagicRequest(MagicTypeRequest.SYNC_DAPP_DATA) {
    override fun toString(): String {
        val verified = "verified: true"
        val projectID = "projectId:'$projectId'"
        val sdkVersion = "sdkVersion:'$sdkVersion'"
        val name = "name:'${metaData.name}'"
        val description = "description:'${metaData.description}'"
        val url = "url:'${metaData.url}'"
        val icons = "icons:['${metaData.icons.first()}']"
        val metadata = "metadata:{$name,$description,$url,$icons}"
        return "{type:'${super.type}',payload:{$verified,$projectID,$sdkVersion,$metadata}"
    }
}

internal data class SyncTheme(val mode: String) : MagicRequest(MagicTypeRequest.SYNC_THEME) {
    override fun toString(): String {
        val tm = "themeMode:\'$mode\'"
        return "{type:\'${super.type}\',payload:{$tm}}"
    }
}
