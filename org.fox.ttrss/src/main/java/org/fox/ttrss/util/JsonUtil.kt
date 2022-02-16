package org.fox.ttrss.util

import android.util.Log
import java.nio.charset.StandardCharsets


object JsonUtil {

    /**
     * 格式化json字符串
     *
     * @param jsonStr 需要格式化的json串
     * @return 格式化后的json串
     */
    fun formatJson(jsonStr: String?): String? {
        if (null == jsonStr || "" == jsonStr) return ""
        val sb = java.lang.StringBuilder()
        var last = '\u0000'
        var current = '\u0000'
        var indent = 0
        for (i in 0 until jsonStr.length) {
            last = current
            current = jsonStr[i]
            when (current) {
                '{', '[' -> {
                    sb.append(current)
                    sb.append('\n')
                    indent++
                    addIndentBlank(sb, indent)
                }
                '}', ']' -> {
                    sb.append('\n')
                    indent--
                    addIndentBlank(sb, indent)
                    sb.append(current)
                }
                ',' -> {
                    sb.append(current)
                    if (last != '\\') {
                        sb.append('\n')
                        addIndentBlank(sb, indent)
                    }
                }
                else -> sb.append(current)
            }
        }
        return sb.toString()
    }

    /**
     * 添加space
     *
     * @param sb
     * @param indent
     */
    private fun addIndentBlank(sb: StringBuilder, indent: Int) {
        for (i in 0 until indent) {
            sb.append('\t')
        }
    }

    /**
     * http 请求数据返回 json 中中文字符为 unicode 编码转汉字转码
     *
     * @param theString
     * @return 转化后的结果.
     */
//    @JvmStatic
//    open fun decodeUnicode(s: String): String? {
//
//        var needToChange = false
//        val numChars: Int = s.length
//        val sb = StringBuffer(if (numChars > 500) numChars / 2 else numChars)
//        var i = 0
//
//        var c: Char
//        var bytes: ByteArray? = null
//        while (i < numChars) {
//            c = s[i]
//            when (c) {
//                '+' -> {
//                    sb.append(' ')
//                    i++
//                    needToChange = true
//                }
//                '%' -> {
//                    /*
//                 * Starting with this instance of %, process all
//                 * consecutive substrings of the form %xy. Each
//                 * substring %xy will yield a byte. Convert all
//                 * consecutive  bytes obtained this way to whatever
//                 * character(s) they represent in the provided
//                 * encoding.
//                 */
//                    try {
//                        // (numChars-i)/3 is an upper bound for the number
//                        // of remaining bytes
//                        if (bytes == null) bytes = ByteArray((numChars - i) / 3)
//                        var pos = 0
//                        while (i + 2 < numChars && c == '%') {
//                            val v: Int = s.substring(i + 1, i + 3).toInt(16)
//                            if (v < 0) throw java.lang.IllegalArgumentException("URLDecoder: Illegal hex characters in escape (%) pattern - negative value")
//                            bytes[pos++] = v.toByte()
//                            i += 3
//                            if (i < numChars) c = s[i]
//                        }
//
//                        // A trailing, incomplete byte encoding such as
//                        // "%x" will cause an exception to be thrown
//                        if (i < numChars && c == '%') throw java.lang.IllegalArgumentException(
//                            "URLDecoder: Incomplete trailing escape (%) pattern"
//                        )
//                        sb.append(String(bytes, 0, pos, StandardCharsets.UTF_8))
//                    } catch (e: NumberFormatException) {
//                        throw java.lang.IllegalArgumentException(
//                            "URLDecoder: Illegal hex characters in escape (%) pattern - "
//                                    + e.message
//                        )
//                    }
//                    needToChange = true
//                }
//                else -> {
//                    sb.append(c)
//                    i++
//                }
//            }
//        }
//
//        return (if (needToChange) sb.toString() else s)
//    }

    @JvmStatic
    open fun decodeUnicode(theString: String): String? {
//        var theString = str.replace("%&#", "% &#")
//        var theString = str.replace("%(?![0-9a-fA-F]{2})", "%25")
//        theString = theString.replace("\\+", "%2B")
        Log.d("test", theString)
        var aChar: Char
        val len = theString.length
        val outBuffer = StringBuffer(len)
        var x = 0
        while (x < len) {
            aChar = theString[x++]
            if (aChar == '\\') {
                aChar = theString[x++]
                if (aChar == 'u') {
                    var value = 0
                    for (i in 0..3) {
                        aChar = theString[x++]
                        value = when (aChar) {
                            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> (value shl 4) + aChar.toInt() - '0'.toInt()
                            'a', 'b', 'c', 'd', 'e', 'f' -> (value shl 4) + 10 + aChar.toInt() - 'a'.toInt()
                            'A', 'B', 'C', 'D', 'E', 'F' -> (value shl 4) + 10 + aChar.toInt() - 'A'.toInt()
                            else -> throw IllegalArgumentException(
                                "Malformed   \\uxxxx   encoding."
                            )
                        }
                    }
                    outBuffer.append(value.toChar())
                } else {
                    if (aChar == 't') aChar = '\t' else if (aChar == 'r') aChar =
                        '\r' else if (aChar == 'n') aChar = '\n' else if (aChar == 'f') aChar = '\u000C'
                    outBuffer.append(aChar)
                }
            } else outBuffer.append(aChar)
        }
        return outBuffer.toString()
    }
}