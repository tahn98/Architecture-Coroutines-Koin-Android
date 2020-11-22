package com.sg.base.view

import com.sg.base.R
import com.sg.base.base.BaseActivity
import com.sg.base.databinding.ActivityLoginBinding
import org.jetbrains.anko.startActivity

class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    override val layoutId: Int
        get() = R.layout.activity_login

    override fun bindView() {
        viewBinding.btnLogin.setOnClickListener {
            startActivity<MainActivity>()
        }
    }
}