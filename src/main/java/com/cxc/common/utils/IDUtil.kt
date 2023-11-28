package com.cxc.common.utils

import android.app.Application
import android.content.Context
import androidx.compose.ui.layout.Layout
import com.cxc.common.log.YLog
import com.github.gzuliyujiang.oaid.DeviceID
import com.github.gzuliyujiang.oaid.DeviceIdentifier
import com.github.gzuliyujiang.oaid.IGetter
import kotlinx.coroutines.withTimeoutOrNull
import java.lang.Exception

/** @date: 2023/11/28 @time: 10:38 @author:cxc @description: */
object IDUtil {

    var deviceID: String? = null
    private const val TAG = "OAID"

     fun getClientId(context: Context): String {
        try {
            if (deviceID.isNullOrEmpty().not()) {
                return deviceID?:""
            }
            val clientId = DeviceID.getClientId()
            if (clientId.isNullOrEmpty().not()) {
                deviceID = clientId
                return clientId
            }
            synchronized(this){
                if (DeviceID.supportedOAID(context)) {
                    DeviceID.getOAID(context, object : IGetter {
                        override fun onOAIDGetComplete(result: String?) {
                            YLog.d(TAG,"OAIDGetComplete ${result}")
                            deviceID = if (isValid(result)) {
                                YLog.v(TAG,"is valid ${result}")
                                result
                            }else{
                                YLog.w(TAG,"is not valid ${result}")
                                DeviceID.getUniqueID(context)
                            }
                        }

                        override fun onOAIDGetError(error: Exception?) {
                            YLog.d(TAG,"OAIDGetError ${error}")
                            deviceID = DeviceID.getUniqueID(context)
                        }
                    })
                } else {
                    return DeviceID.getUniqueID(context)
                }
            }
            YLog.d(TAG,"oaid 返回 ${deviceID}")
            return deviceID?:""
        }catch (e: Exception){
            return ""
        }
    }

      fun getUniqueID(context: Context): String {
        return DeviceID.getUniqueID(context)
    }



    private fun isValid(input: String?): Boolean {
        if (input.isNullOrEmpty()) return false
        return input.replace("-","").all { it == '0' }.not()
    }
}