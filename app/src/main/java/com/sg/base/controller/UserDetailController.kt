package com.sg.base.controller

import android.os.Bundle
import com.sg.base.R
import com.sg.base.base.BaseController
import com.sg.base.databinding.ControllerUserDetailBinding

class UserDetailController(private val bundle: Bundle): BaseController<ControllerUserDetailBinding>() {
    override val layoutId: Int
        get() = R.layout.controller_user_detail

    override fun bindingView() {
        viewBinding.user = bundle.getParcelable("User")
    }
}