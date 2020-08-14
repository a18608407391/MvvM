package com.zl.zlibrary.binding.ViewAdapter.View;

import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.zl.zlibrary.binding.BindingCommand;

public class ViewAdapter {




    public static long LastClickTime = 0L;

    @BindingAdapter("singleClick")
    public static void setOnSingleClick(TextView view, BindingCommand action){
          view.setOnClickListener(v -> {
              if(System.currentTimeMillis() - LastClickTime>1000){
                  if(action!=null){
                      action.execute(v.getId());
                  }
                  LastClickTime = System.currentTimeMillis();
              }
          });
    }
}
