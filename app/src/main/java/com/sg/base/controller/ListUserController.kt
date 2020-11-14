package com.sg.base.controller

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.MergeAdapter
import com.sg.base.R
import com.sg.base.adapter.MessagePagedAdapter
import com.sg.base.base.BaseActivity
import com.sg.base.base.BaseController
import com.sg.base.base.LoadStateAdapter
import com.sg.base.databinding.ControllerListUserBinding
import com.sg.base.viewmodel.AuthViewModel
import com.sg.core.CoreApplication
import com.sg.core.model.Result
import com.sg.core.param.LoginParam
import org.koin.java.KoinJavaComponent.inject

class ListUserController : BaseController<ControllerListUserBinding>() {

    override val layoutId: Int
        get() = R.layout.controller_list_user


    private val authViewModel: AuthViewModel by inject(AuthViewModel::class.java)

    private var adapter: MessagePagedAdapter? = null
    private var loadStateAdapter: LoadStateAdapter? = null
    private var mergeAdapter: MergeAdapter? = null


    override fun bindingView() {
        adapter = MessagePagedAdapter {
            val nextController = UserDetailController(Bundle().apply {
                this.putParcelable("User", it)
            })
            (activity as? BaseActivity<*>)?.changeScreen(nextController)
        }
        loadStateAdapter = LoadStateAdapter()
        mergeAdapter = MergeAdapter(adapter, loadStateAdapter)

        val gridLayoutManager = GridLayoutManager(activity, 2)
        viewBinding.rvMessage.layoutManager = gridLayoutManager
        viewBinding.rvMessage.adapter = mergeAdapter
        gridLayoutManager.spanSizeLookup = object :
            GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when {
                    loadStateAdapter?.loadState is Result.Error -> return gridLayoutManager.spanCount

                    ((gridLayoutManager.itemCount - 1 == position) && loadStateAdapter?.loadState is Result.LoadingMore) -> {
                        return gridLayoutManager.spanCount
                    }
                    else -> 1
                }
            }
        }

        authViewModel.login(
            LoginParam(
                "jason@vinova.sg",
                "123123",
                "dGKlJJU1lCc:APA91bGZTz25rKtcb5WobysyPQSUp0Bfp4w1hblFjgWQeGdCEZwgFmRTCTQX9vhDk2WazWcvwpOHn8MV4NyTjrgE5vFEraxP5GbAMOnqYmo6FyVGy924yS98pEYSJXBJZ_5g_56nIFuC"
            )
        )

    }

    override fun setUpViewModel() {
        authViewModel.loginLiveData.observe(this, {
            CoreApplication.instance.saveUser(it)
            authViewModel.messagePaging()
        })

        authViewModel.loadStateLiveData.observe(this, {
            loadStateAdapter?.loadState = it
        })

        authViewModel.messagesLiveData.observe(this, {
            adapter?.submitList(it)
        })

    }
}