package com.zl.zlibrary.base.adapter.base.itembindings;

import androidx.annotation.NonNull;

import com.zl.zlibrary.base.adapter.base.ItemBinding;
import com.zl.zlibrary.base.adapter.base.OnItemBind;


/**
 * An {@link OnItemBind} that selects item views by delegating to each item. Items must implement
 * {@link ItemBindingModel}.
 */
public class OnItemBindModel<T extends ItemBindingModel> implements OnItemBind<T> {

    @Override
    public void onItemBind(@NonNull ItemBinding itemBinding, int position, T item) {
        item.onItemBind(itemBinding);
    }
}
