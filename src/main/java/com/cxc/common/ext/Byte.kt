package com.cxc.common.ext

import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 *@Date: 2023/8/29
 *@Time: 14:18
 *@Author:cxc
 *@Description:
 */

/**
 * 截取byte数组   不改变原数组
 * @param offset 偏差值（索引）
 * @param length 长度
 * @return 截取后的数组
 */
fun ByteArray.subByte(offset: Int, length: Int): ByteArray {
    val b1 = ByteArray(length)
    System.arraycopy(this, offset, b1, 0, length)
    return b1
}

fun ByteArray.toInt(): Int {
    if (this.size < 4) return 0
    return ByteBuffer.wrap(this).order(ByteOrder.LITTLE_ENDIAN).int
}