package com.zl.zlibrary.Utils.Glide.imagei;

import android.graphics.Bitmap;

/**
 * Created by doudou on 2017/5/2.
 */

public interface ImageDownLoadCallBack {

    void onDownLoadSuccess(Bitmap bitmap);

    void onDownLoadFailed();
}
