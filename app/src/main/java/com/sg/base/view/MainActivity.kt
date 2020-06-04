package com.sg.base.view

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.MergeAdapter
import coil.api.load
import com.sg.base.R
import com.sg.base.adapter.MessagePagedAdapter
import com.sg.base.base.BaseActivity
import com.sg.base.base.LoadStateAdapter
import com.sg.base.databinding.ActivityMainBinding
import com.sg.base.ext.hasReadStoragePermission
import com.sg.base.ext.requestReadAndWriteStoragePermission
import com.sg.base.view.adapter.NowPlayingAdapter
import com.sg.base.viewmodel.AuthViewModel
import com.sg.base.viewmodel.MovieViewModel
import com.sg.core.CoreApplication
import com.sg.core.model.Result
import com.sg.core.param.LoginParam
import com.sg.core.util.DatabaseUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
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

    private val movieViewModel: MovieViewModel by viewModel()
    private lateinit var nowPlayingAdapter: NowPlayingAdapter


    override val layoutId: Int
        get() = R.layout.activity_main

    override fun bindView() {

        if (!hasReadStoragePermission()) {
            requestReadAndWriteStoragePermission(999)
        }
        nowPlayingAdapter = NowPlayingAdapter { movie, index ->

        }
        viewBinding.rcv.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            adapter = nowPlayingAdapter
        }
        movieViewModel.getMovies(1)
    }


    override fun bindViewModel() {
        movieViewModel.liveData.observe(this, Observer {
            nowPlayingAdapter.submitList(it.results)
        })
    }
}
