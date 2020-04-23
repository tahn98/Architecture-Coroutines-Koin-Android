package com.sg.base.view

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.MergeAdapter
import coil.api.load
import com.sg.base.R
import com.sg.base.adapter.MessagePagedAdapter
import com.sg.base.base.BaseActivity
import com.sg.base.base.LoadStateAdapter
import com.sg.base.databinding.ActivityMainBinding
import com.sg.base.ext.hasReadStoragePermission
import com.sg.base.ext.requestReadAndWriteStoragePermission
import com.sg.base.viewmodel.AuthViewModel
import com.sg.core.CoreApplication
import com.sg.core.model.Result
import com.sg.core.param.LoginParam
import com.sg.core.util.DatabaseUtil
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

    private var adapter: MessagePagedAdapter? = null
    private var loadStateAdapter: LoadStateAdapter? = null
    private var mergeAdapter: MergeAdapter? = null


    override val layoutId: Int
        get() = R.layout.activity_main

    override fun bindView() {

        if (!hasReadStoragePermission()) {
            requestReadAndWriteStoragePermission(999)
        }

        adapter = MessagePagedAdapter()
        loadStateAdapter = LoadStateAdapter()
        mergeAdapter = MergeAdapter(adapter, loadStateAdapter)

//        viewBinding.rvMessage.layoutManager =
//            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val gridLayoutManager = GridLayoutManager(this, 2)
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

//        viewBinding.ivImageView.load("https://scontent.fsgn2-4.fna.fbcdn.net/v/t1.0-9/s960x960/83503315_2532361526892826_6398406891898142720_o.jpg?_nc_cat=109&_nc_sid=110474&_nc_ohc=fM8c1-ZvmgYAX8BWKT-&_nc_ht=scontent.fsgn2-4.fna&_nc_tp=7&oh=a0ded138018389a32568ddda8f350c0d&oe=5EC3FE42")
        viewBinding.ivImageView.load(R.drawable.image)
        viewBinding.ivImageView.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 999)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 999 && resultCode == Activity.RESULT_OK) {
            viewBinding.ivImageView.load(data?.data?.path.toString())
        }
    }

    override fun bindViewModel() {
        authViewModel.loginLiveData.observe(this, Observer {
            CoreApplication.instance.saveUser(it)
            viewBinding.data = it
            Toast.makeText(this, it.email, Toast.LENGTH_SHORT).show()
            authViewModel.messagePaging()

            DatabaseUtil().exportDB(this, "User")
//            authViewModel.messagePagingDB()
        })

        authViewModel.loadStateLiveData.observe(this, Observer {
            loadStateAdapter?.loadState = it
        })

        authViewModel.messagesLiveData.observe(this, Observer {
            adapter?.submitList(it)
        })
    }
}
