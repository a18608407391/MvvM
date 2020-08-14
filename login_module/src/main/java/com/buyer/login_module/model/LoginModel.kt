package com.buyer.login_module.model

import com.zl.zlibrary.manager.BaseResponse


class LoginModel : BaseResponse<String>(
) {
    override fun isSuccess() = code == 10000
}