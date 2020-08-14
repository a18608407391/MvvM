package com.buyer.zcommon_module.Entity




class BaseRequestParam internal constructor(requestBuilder: RequestBuilder){


    @get:JvmName("id") val id: Int = requestBuilder.id


    class RequestBuilder{
            var id   = 0

            fun setId(id:Int): RequestBuilder {
                this.id = id
                return this
            }
            fun build(): BaseRequestParam {
                return BaseRequestParam(this)
            }
        }
}