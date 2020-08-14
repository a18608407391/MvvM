package com.zl.zlibrary.ext

import java.io.*
import java.util.*

val CPU_ARCHITECTURE_TYPE_32 = "32"
val CPU_ARCHITECTURE_TYPE_64 = "64"

/** ELF文件头 e_indent[]数组文件类标识索引  */
private val EI_CLASS = 4
/** ELF文件头 e_indent[EI_CLASS]的取值：ELFCLASS32表示32位目标  */
private val ELFCLASS32 = 1
/** ELF文件头 e_indent[EI_CLASS]的取值：ELFCLASS64表示64位目标  */
private val ELFCLASS64 = 2

/** The system property key of CPU arch type  */
private val CPU_ARCHITECTURE_KEY_64 = "ro.product.cpu.abilist64"

/** The system libc.so file path  */
private val SYSTEM_LIB_C_PATH = "/system/lib/libc.so"
private val SYSTEM_LIB_C_PATH_64 = "/system/lib64/libc.so"
private val PROC_CPU_INFO_PATH = "/proc/cpuinfo"

fun check_or_not_Cpu_x86(): Boolean {
    return getSystemProperty("ro.product.cpu.abi", "arm").contains("x86")
}

fun getCpuArchType(): String {
    if (getSystemProperty(CPU_ARCHITECTURE_KEY_64, "").isNotEmpty()) {
        return CPU_ARCHITECTURE_TYPE_64;
    } else if (check_CPU_Info64()) {
        return CPU_ARCHITECTURE_TYPE_64;
    } else if (isLibc64()) {
        return CPU_ARCHITECTURE_TYPE_64;
    } else {
        return CPU_ARCHITECTURE_TYPE_32;
    }
}

fun check_CPU_Info64():Boolean{
    val cpuInfo = File(PROC_CPU_INFO_PATH)
    if (cpuInfo != null && cpuInfo.exists()) {
        var inputStream: InputStream? = null
        var bufferedReader: BufferedReader? = null
        try {
            inputStream = FileInputStream(cpuInfo)
            bufferedReader = BufferedReader(InputStreamReader(inputStream), 512)
            val line = bufferedReader!!.readLine()
            if (line != null && line!!.length > 0 && line!!.toLowerCase(Locale.US).contains("arch64")) {
                return true
            } else {
            }
        } catch (t: Throwable) {
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader!!.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                if (inputStream != null) {
                    inputStream!!.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
    return false
}


fun isLibc64():Boolean{
    val libcFile = File(SYSTEM_LIB_C_PATH)
    if (libcFile != null && libcFile.exists()) {
        val header = read_ELF_Headr_IndentArray(libcFile)
        if (header != null && header!![EI_CLASS].toInt() == ELFCLASS64) {
            return true
        }
    }

    val libcFile64 = File(SYSTEM_LIB_C_PATH_64)
    if (libcFile64 != null && libcFile64.exists()) {
        val header = read_ELF_Headr_IndentArray(libcFile64)
        if (header != null && header!![EI_CLASS].toInt() == ELFCLASS64) {
            return true
        }
    }
    return false
}


fun read_ELF_Headr_IndentArray(libFile:File): ByteArray {
    if (libFile != null && libFile.exists()) {
        var inputStream: FileInputStream? = null
        try {
            inputStream = FileInputStream(libFile)
            if (inputStream != null) {
                val tempBuffer = ByteArray(16)
                val count = inputStream.read(tempBuffer, 0, 16)
                if (count == 16) {
                    return tempBuffer
                } else {
                }
            }
        } catch (t: Throwable) {
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }
    return null!!
}