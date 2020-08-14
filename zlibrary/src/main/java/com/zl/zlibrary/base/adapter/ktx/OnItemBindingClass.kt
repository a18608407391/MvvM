@file:Suppress("NOTHING_TO_INLINE")

package com.zl.zlibrary.base.adapter.ktx

import androidx.annotation.LayoutRes
import com.zl.zlibrary.base.adapter.base.ItemBinding
import com.zl.zlibrary.base.adapter.base.itembindings.OnItemBindClass

/**
 * Maps the given type to the given id and layout.
 *
 */
inline fun <reified T> OnItemBindClass<in T>.map(variableId: Int, @LayoutRes layoutRes: Int) {
    map(T::class.java, variableId, layoutRes)
}

/**
 * Maps the given type to the given callback.
 *
 *
 */

inline fun <reified T> OnItemBindClass<in T>.map(
    noinline onItemBind: (
        @ParameterName("itemBinding") ItemBinding<in T>,
        @ParameterName("position") Int,
        @ParameterName("item") T
    ) -> Unit
) {
    map(T::class.java) { itemBinding, position, item ->
        onItemBind(
            itemBinding as ItemBinding<in T>,
            position,
            item
        )
    }
}
