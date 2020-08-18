package com.zl.zlibrary.annotation.inject;

import android.util.Log;

import androidx.fragment.app.Fragment;

import com.zl.zlibrary.base.BaseViewModel;
import com.zl.zlibrary.base.IBaseViewModel;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class AnnotationInject {


    public static void inject(BaseViewModel model) {

    IBaseViewModel inf = (IBaseViewModel) Proxy.newProxyInstance(model.getClass().getClassLoader(), new Class[]{IBaseViewModel.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                Log.e("result","动态代理的代理");
                return method.invoke(model,args);
            }
        });
        inf.onSingle("123123213");
    }


}
