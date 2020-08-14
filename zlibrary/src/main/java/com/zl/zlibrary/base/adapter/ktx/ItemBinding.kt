@file:Suppress("NOTHING_TO_INLINE")

package com.zl.zlibrary.base.adapter.ktx

import androidx.annotation.LayoutRes
import com.zl.zlibrary.base.adapter.base.ItemBinding
import com.zl.zlibrary.base.adapter.base.OnItemBind

/**
 * Creates an `ItemBinding` with the given id and layout.
 *
 * @see ItemBinding.of
 */
inline fun <T> itemBindingOf(variableId: Int, @LayoutRes layoutRes: Int): ItemBinding<T> =
    ItemBinding.of(variableId, layoutRes)

/**
 * Creates an `ItemBinding` with the given callback.
 *
 * @see ItemBinding.of
 */
inline fun <T> itemBindingOf(
    noinline onItemBind: (
        @ParameterName("itemBinding") ItemBinding<in T>,
        @ParameterName("position") Int,
        @ParameterName("item") T
    ) -> Unit
): ItemBinding<T> = ItemBinding.of(onItemBind)

/**
 * Converts an `OnItemBind` to a `ItemBinding`.
 *
 * @see ItemBinding.of
 */
inline fun <T> OnItemBind<T>.toItemBinding(): ItemBinding<T> =
    ItemBinding.of(this)

