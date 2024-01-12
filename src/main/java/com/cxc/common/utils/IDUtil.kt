package com.cxc.common.utils

import android.app.Application
import android.content.Context
import androidx.compose.ui.layout.Layout
import com.cxc.common.log.YLog
import com.github.gzuliyujiang.oaid.DeviceID
import com.github.gzuliyujiang.oaid.DeviceIdentifier
import com.github.gzuliyujiang.oaid.IGetter
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import java.lang.Exception

/** @date: 2023/11/28 @time: 10:38 @author:cxc @description: */
object IDUtil {

    var deviceID: String? = null
    private const val TAG = "OAID"


    fun getClientIdAsync(context: Context, scope: CoroutineScope): CompletableDeferred<String> {
        val deferred = CompletableDeferred<String>()
        try {
            if (deviceID.isNullOrEmpty().not()) {
                deferred.complete(deviceID ?: "")
                return deferred
            }
            val clientId = DeviceID.getClientId()
            if (clientId.isNullOrEmpty().not()) {
                deviceID = clientId
                deferred.complete(clientId)
                return deferred
            }

            scope.launch(Dispatchers.IO) {
                if (DeviceID.supportedOAID(context)) {
                    DeviceID.getOAID(context, object : IGetter {
                        override fun onOAIDGetComplete(result: String?) {
                            deviceID = if (isValid(result)) {
                                result
                            } else {
                                getUniqueID(context)
                            }
                            deferred.complete(deviceID ?: "")
                        }

                        override fun onOAIDGetError(error: Exception?) {
                            deviceID = getUniqueID(context)
                            deferred.complete(deviceID ?: "")
                        }
                    })
                } else {
                    deferred.complete(getUniqueID(context))
                }
            }
        } catch (e: Exception) {
            deferred.complete("")
        }

        return deferred

    }

    fun getClientId(context: Context): String {
        return deviceID ?: getUniqueID(context)
    }

    /**
     * Get unique id.
     * 在获取不到oaid的情况下 返回这个
     * @param context Context
     * @return
     */
    fun getUniqueID(context: Context): String {
        val uniqueID = DeviceID.getUniqueID(context)
        if (!uniqueID.isNullOrEmpty()) {
            deviceID = uniqueID
            return uniqueID
        }
        val hardwareId = DeviceID.getAndroidID(context) + DeviceID.getPseudoID()
        deviceID = hardwareId
        return hardwareId
    }


    private fun isValid(input: String?): Boolean {
        if (input.isNullOrEmpty()) return false
        return input.replace("-", "").all { it == '0' }.not()
    }
}