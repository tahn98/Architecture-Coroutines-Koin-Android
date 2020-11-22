package com.sg.base.view

import android.widget.Toast
import androidx.lifecycle.Observer
import com.sg.base.R
import com.sg.base.base.BaseActivity
import com.sg.base.databinding.ActivityLoginBinding
import com.sg.base.viewmodel.AuthViewModel
import com.sg.core.CoreApplication
import com.sg.core.param.LoginParam
import com.sg.core.util.DatabaseUtil
import org.jetbrains.anko.startActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    private val authViewModel: AuthViewModel by viewModel()


    override val layoutId: Int
        get() = R.layout.activity_login

    override fun bindView() {
        viewBinding.btnLogin.setOnClickListener {
            authViewModel.login(
                LoginParam(
                    "jason@vinova.sg",
                    "123123",
                    "dGKlJJU1lCc:APA91bGZTz25rKtcb5WobysyPQSUp0Bfp4w1hblFjgWQeGdCEZwgFmRTCTQX9vhDk2WazWcvwpOHn8MV4NyTjrgE5vFEraxP5GbAMOnqYmo6FyVGy924yS98pEYSJXBJZ_5g_56nIFuC"
                )
            )
        }
    }

    override fun bindViewModel() {
        authViewModel.loginLiveData.observe(this, Observer {
            CoreApplication.instance.saveUser(it)
            Toast.makeText(this, it.email, Toast.LENGTH_SHORT).show()
//            authViewModel.messagePaging()

            DatabaseUtil().exportDB(this, "User")
            startActivity<MainActivity>()
//            authViewModel.messagePagingDB()
        })
    }
}