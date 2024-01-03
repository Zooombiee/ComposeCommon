package com.cxc.common.utils


import android.util.Log
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.Inet4Address
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.NetworkInterface
import java.net.Socket
import java.net.URL
import java.nio.ByteBuffer
import java.util.*

class NetworkAddressUtils {
    companion object {
        private val instance:
                NetworkAddressUtils by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            NetworkAddressUtils()
        }

        fun get() = instance
    }

    /** *
     *
     * @param useIPv4 true ipv4，false ipv6
     * @return
     */
    fun getIPAddress(useIPv4: Boolean): String? {
        try {
            val interfaces: List<NetworkInterface> =
                Collections.list(NetworkInterface.getNetworkInterfaces())
            for (networkInterface in interfaces) {
                val addresses: List<InetAddress> =
                    Collections.list(networkInterface.getInetAddresses())
                for (address in addresses) {
                    if (!address.isLoopbackAddress()) {
                        val sAddr: String = address.getHostAddress()
                        val isIPv4 = sAddr.indexOf(':') < 0
                        if (useIPv4) {
                            if (isIPv4) {
                                return sAddr
                            }
                        }
                    }
                }
            }
        } catch (ignored: java.lang.Exception) {
        }
        return "0.0.0.0"
    }

    /**
     * 打开网络调试
     * @return 是否成功
     */
    fun openNetworkDebugging(): Boolean {
        var os: DataOutputStream? = null
        return try {
            val process = Runtime.getRuntime().exec("su")
            os = DataOutputStream(process.outputStream)
            os.writeBytes("setprop service.adb.tcp.port 5555\n")
            os.writeBytes("start adbd\n")
            os.flush()
            true
        } catch (e: Exception) {
            false
        } finally {
            try {
                os?.close()
            } catch (e: IOException) {
            }
        }
    }

    /**
     * 关闭网络调试
     * @return 是否成功
     */
    fun closeNetworkDebugging(): Boolean {
        var os: DataOutputStream? = null
        return try {
            val process = Runtime.getRuntime().exec("su")
            os = DataOutputStream(process.outputStream)
            os.writeBytes("setprop service.adb.tcp.port 5555\n")
            os.writeBytes("stop adbd\n")
            os.flush()
            true
        } catch (e: Exception) {
            false
        } finally {
            try {
                os?.close()
            } catch (e: IOException) {
            }
        }
    }

    /**
     * Is a d b wireless enabled
     *获取wifi adb状态
     * @param context
     * @return
     */
    suspend fun isADBWirelessEnabled(): Boolean {
        return try {
            val socket = Socket()
            socket.connect(InetSocketAddress(getLocalIpAddress(), 5555), 2000) // 2秒连接超时
            socket.close()
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Get local ip address
     * 获取Lan 地址
     * @return
     */
      fun getLocalIpAddress(): String {
        try {
            val networkInterfaces = NetworkInterface.getNetworkInterfaces()
            while (networkInterfaces.hasMoreElements()) {
                val networkInterface = networkInterfaces.nextElement()
                val addresses = networkInterface.inetAddresses
                while (addresses.hasMoreElements()) {
                    val inetAddress = addresses.nextElement()
                    if (!inetAddress.isLoopbackAddress && inetAddress.address.size == 4) {
                        val ipAddress = inetAddress.hostAddress
                        if (isLocalIPAddress(ipAddress)) {
                            return ipAddress?:""
                        }
                    }
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return ""
    }

    private fun isLocalIPAddress(ipAddress: String): Boolean {
        // 在这里进行 IP 地址范围的判断，判断是否属于局域网范围
        // 返回 true 表示是局域网地址，返回 false 表示不是局域网地址
        // 例如：192.168.0.0 到 192.168.255.255、10.0.0.0 到 10.255.255.255 等
        return ipAddress.startsWith("192.168.") || ipAddress.startsWith("10.")
    }

    /**
     * Get public i pv4
     * 获取公网IPv4
     * @return
     */
    suspend fun getPublicIPv4(): String? {
        // if (NetworkUtils.isWifiConnected()) {
            return try {
                val url = URL("https://ipv4.icanhazip.com/") // 使用ipify提供的API获取IPv4地址
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }
                reader.close()
                response.toString()
            } catch (e: Exception) {
                Log.w("ipv4","无法获取IP地址：$e")
                e.printStackTrace()
                "0.0.0.0"
            }
        // }else{
        //     return getIPv4Address()
        // }
    }

     fun getIPv4Address(): String {
        try {
            val networkInterfaces = NetworkInterface.getNetworkInterfaces()
            while (networkInterfaces.hasMoreElements()) {
                val networkInterface = networkInterfaces.nextElement()
                val addresses = networkInterface.inetAddresses
                while (addresses.hasMoreElements()) {
                    val address = addresses.nextElement()
                    if (address is Inet4Address && !address.isLoopbackAddress()&& (!address.isLinkLocalAddress())) {
                        return address.getHostAddress()?:"0.0.0.0" // 这是 IPv4 地址的点分十进制表示
                    }
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return "0.0.0.0"
    }

    /**
     * Get public i pv6
     * 获取公网IPv6
     * @return
     */
    fun getIPv6Address(): String {
        return try {
            val url = URL("https://ipv6.icanhazip.com/") // 使用ipify提供的API获取IPv4地址
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            val response = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                response.append(line)
            }
            reader.close()
            response.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.w("ipv6","无法获取IP地址：$e")
            getIPv6AddresByAPI6()
        }
    }

    private fun getIPv6AddresByAPI6(): String {
        return try {
            val url = URL("https://api6.ipify.org?") // 使用ipify提供的API获取IPv4地址
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            val response = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                response.append(line)
            }
            reader.close()
            response.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.w("ipv6","无法获取IP地址：$e")
            return ""
        }
    }

    private fun  validateV6(ipAddress: String?): String {
        if (ipAddress!=null && ipAddress.contains("%")){
           val splits =  ipAddress.split("%")
            val s = splits[0]
            if (s!=null && s.contains(":")){
                return s
            }
        }
        return ipAddress?:"0.0.0.0"
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    fun intIP2StringIP(ip: Int): String? {
        return (ip and 0xFF).toString() + "." +
                (ip shr 8 and 0xFF) + "." +
                (ip shr 16 and 0xFF) + "." +
                (ip shr 24 and 0xFF)
    }


    private fun getSubnetMask(prefixLength: Short): String {
        val subnetMaskInt = 0xffffffff.toInt() shl (32 - prefixLength)
        val bytes = ByteArray(4)
        ByteBuffer.wrap(bytes).asIntBuffer().put(subnetMaskInt)
        return "${bytes[0].toInt() and 0xFF}.${bytes[1].toInt() and 0xFF}.${bytes[2].toInt() and 0xFF}.${bytes[3].toInt() and 0xFF}"
    }

     fun getLocalAddress(): InetAddress? {
        try {
            val ifaces = NetworkInterface.getNetworkInterfaces()
            while (ifaces.hasMoreElements()) {
                val iface = ifaces.nextElement()
                val iaddresses = iface.inetAddresses
                while (iaddresses.hasMoreElements()) {
                    val iaddress = iaddresses.nextElement()
                    if (Class.forName("java.net.Inet4Address").isInstance(iaddress)) {
                        if (!iaddress.isLoopbackAddress && !iaddress.isLinkLocalAddress) {
                            return iaddress
                        }
                    }
                }
            }
        } catch (e: java.lang.Exception) {
            Log.e("network", e.message, e)
        }
        return null
    }
}