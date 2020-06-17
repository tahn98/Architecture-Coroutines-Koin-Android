package com.sg.base.view

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.MergeAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.sg.base.R
import com.sg.base.adapter.MessagePagingAdapter
import com.sg.base.base.BaseActivity
import com.sg.base.base.BaseLoadStateAdapter
import com.sg.base.databinding.ActivityMainBinding
import com.sg.base.ext.hasReadStoragePermission
import com.sg.base.ext.requestReadAndWriteStoragePermission
import com.sg.base.viewmodel.AuthViewModel
import com.sg.core.CoreApplication
import com.sg.core.model.Result
import com.sg.core.param.LoginParam
import com.sg.core.util.DatabaseUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Skeleton of an Android Things activity.
 *
 * Android Things peripheral APIs are accessible through the PeripheralManager
 * For example, the snippet below will open a GPIO pin and set it to HIGH:
 *
 * val manager = PeripheralManager.getInstance()
 * val gpio = manager.openGpio("BCM6").apply {
 *     setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
 * }
 * gpio.value = true
 *
 * You can find additional examples on GitHub: https://github.com/androidthings
 */
class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val authViewModel: AuthViewModel by viewModel()

    private var adapter: MessagePagingAdapter? = null

    override val layoutId: Int
        get() = R.layout.activity_main

    override fun bindView() {

        adapter = MessagePagingAdapter()
        viewBinding.rvMessage.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
//        val gridLayoutManager = GridLayoutManager(this, 2)
//        viewBinding.rvMessage.layoutManager = gridLayoutManager
        viewBinding.rvMessage.adapter = adapter?.withLoadStateFooter(
            footer = BaseLoadStateAdapter() {
                adapter?.retry()
            }
        )
//        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
//            override fun getSpanSize(position: Int): Int {
//                return when {
//                    (gridLayoutManager.itemCount - 1 == position) -> {
//                        return gridLayoutManager.spanCount
//                    }
//                    else -> 1
//                }
//            }
//        }

        lifecycleScope.launch {
            adapter?.loadStateFlow?.collectLatest { loadStates ->
                viewBinding.swipeRefresh.isRefreshing = loadStates.refresh is LoadState.Loading
            }
        }

        viewBinding.swipeRefresh.setOnRefreshListener { adapter?.refresh() }

//

        authViewModel.login(
            LoginParam(
                "jason@vinova.sg",
                "123123",
                "dGKlJJU1lCc:APA91bGZTz25rKtcb5WobysyPQSUp0Bfp4w1hblFjgWQeGdCEZwgFmRTCTQX9vhDk2WazWcvwpOHn8MV4NyTjrgE5vFEraxP5GbAMOnqYmo6FyVGy924yS98pEYSJXBJZ_5g_56nIFuC"
            )
        )

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
            viewBinding.data = it
        })

        lifecycleScope.launch {
            authViewModel.messageFlow.collectLatest { paging ->
                adapter?.submitData(paging)
            }
        }
    }
}
