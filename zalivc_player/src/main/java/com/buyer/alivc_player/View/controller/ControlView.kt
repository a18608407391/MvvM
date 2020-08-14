package com.buyer.alivc_player.View.controller

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.buyer.alivc_player.R
import com.buyer.alivc_player.BR




class ControlView  @JvmOverloads constructor(mContext:Context, attributes: AttributeSet, def:Int) :RelativeLayout(mContext,attributes,0){

    init {

        initView()
    }

    var mInflater   :LayoutInflater= mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    private fun initView() {

//      var binding =    DataBindingUtil.inflate<ViewDataBinding>(mInflater, R.layout.alivc_view_controller,this,true)
//        var model    = ControllerViewModel()
//        binding.setVariable(BR.controller_model,model)

    }


    interface OnShowMoreClickListener {
        fun showMore()
    }


    var mOnShowMoreClickListener  :OnShowMoreClickListener ? = null
    fun setOnShowMoreClickListener(
        listener: OnShowMoreClickListener
    ) {
        this.mOnShowMoreClickListener = listener
    }

}