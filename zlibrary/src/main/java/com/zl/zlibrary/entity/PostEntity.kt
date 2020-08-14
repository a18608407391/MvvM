package com.zl.library.Entity

import com.zl.library.inteface.Builder.IBuilder
import com.zl.library.inteface.Builder.ICreateWork


class PostEntity : ICreateWork<PostEntity> {


    override fun create(): PostEntity {
        return PostEntity()
    }

    class Builder : IBuilder<PostEntity> {
        override fun build(): PostEntity {
            return PostEntity().create()
        }

        var id: String? = null
        var password: String? = null
        var username: String? = null
        fun setId(id: String): Builder {
            this.id = id
            return this
        }

        fun setPassword(password: String): Builder {
            this.password = password
            return this
        }

        fun setUsername(username: String): Builder {
            this.username = username
            return this
        }
    }
}