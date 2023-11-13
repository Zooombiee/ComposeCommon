package com.cxc.common


/**
 *@Date: 2023/8/2
 *@Time: 11:19
 *@Author:cxc
 *@Description:不同APP的基础配置
 */
object AppConfig {

     var BASE_URL: String = ""

    //加密秘钥
     var ENCRYPT_KEY :String = ""

    //解密秘钥
     var DECRYPT_KEY :String = ""


    fun config(block:AppConfig.()->Unit){
        AppConfig.run {
            block()
        }
    }



}
