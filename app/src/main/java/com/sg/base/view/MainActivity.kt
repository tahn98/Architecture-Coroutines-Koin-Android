package com.sg.base.view

import com.bluelinelabs.conductor.Controller
import com.sg.base.R
import com.sg.base.base.BaseActivity
import com.sg.base.controller.ListUserController
import com.sg.base.databinding.ActivityMainBinding
import com.sg.base.ext.hasReadStoragePermission
import com.sg.base.ext.requestReadAndWriteStoragePermission

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
    override val layoutId: Int
        get() = R.layout.activity_main

    override val containerId: Int
        get() = R.id.container

    override val rootController: Controller?
        get() = ListUserController()

    override fun bindView() {

        if (!hasReadStoragePermission()) {
            requestReadAndWriteStoragePermission(999)
        }
    }
}
