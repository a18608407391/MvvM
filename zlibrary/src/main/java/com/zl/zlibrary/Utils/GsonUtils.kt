package com.zl.library.Utils

import com.google.gson.GsonBuilder


class GsonUtils {


    companion object {

        fun <T : Any> parseJsonWithGson(jsonData: String, type: Class<T>): T {
            var result: T? = null
            if (!jsonData.isNullOrEmpty()) {
                var gson = GsonBuilder().create()
                try {
                    result = gson.fromJson<T>(jsonData, type)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    gson = null
                }
            }
            return result!!
        }
    }
}