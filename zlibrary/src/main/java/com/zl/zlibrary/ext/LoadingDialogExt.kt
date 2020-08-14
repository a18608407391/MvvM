package com.zl.zlibrary.ext

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.zl.zlibrary.R

var progressDialog: Dialog? = null

fun AppCompatActivity.showProgressDialogExt(text: String) {
    if (progressDialog == null) {
        progressDialog = Dialog(this)
        var inflate = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var view = inflate.inflate(R.layout.loading_layout, null, false)
        progressDialog!!.setContentView(view)
        progressDialog!!.setCancelable(false)
        progressDialog!!.setCanceledOnTouchOutside(false)
        progressDialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
    }
    var img = progressDialog!!.findViewById<ImageView>(R.id.progress_animate)
    var title = progressDialog!!.findViewById<TextView>(R.id.progress_title)
    if (!progressDialog!!.isShowing) {
        title!!.text = text
        var d = img.background as AnimationDrawable
        d.start()
        progressDialog!!.show()
    }

}

fun AppCompatActivity.dismissProgressDialogExt() {
    if (progressDialog != null && progressDialog!!.isShowing) {
        progressDialog!!.dismiss()
    }
}