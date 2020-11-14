package com.sg.base.base

import android.app.Activity
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.sg.base.ext.setAutoHideKeyboard
import java.lang.Error

abstract class BaseActivity<ViewBinding : ViewDataBinding> : AppCompatActivity() {
    lateinit var viewBinding: ViewBinding

    @get:LayoutRes
    abstract val layoutId: Int

    @get:LayoutRes
    open val toolbarLayoutId: Int = -1

    @get:IdRes
    open val containerId: Int = -1
    open val rootController: Controller? = null

    protected var router: Router? = null


//    private var listOfPopupDialogFragment: Set<DialogFragment> =
//        CopyOnWriteArraySet<DialogFragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        AppEvent.addPopupEventListener(this)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        viewBinding = DataBindingUtil.setContentView(this, layoutId)
        viewBinding.lifecycleOwner = this
        viewBinding.root.setAutoHideKeyboard(this)

        if (containerId != -1) {
            val container = findViewById<ViewGroup>(containerId)
            router = Conductor.attachRouter(this, container, savedInstanceState)
            if (rootController != null && router?.hasRootController() == false) {
                router?.setRoot(
                    RouterTransaction.with(rootController!!)
                )
            } else {
                throw Error("rootController must not be null")
            }
        }

        bindView()
        bindViewModel()
    }

    fun changeScreen(controller: Controller) {
        router?.pushController(
            RouterTransaction.with(
                controller
            )
        )
    }

    override fun onBackPressed() {
        if (router?.handleBack() == false) {
            super.onBackPressed()
        }
    }

    private fun setupToolbar() {
        if (toolbarLayoutId == -1) return
//        addToolbar(
//            toolbarLayoutId = toolbarLayoutId,
//            viewGroup = viewBinding.root as? ViewGroup,
//            toolbarCallBack = { curActivity, toolbar ->
//                toolbarFunc(curActivity, toolbar)
//            })
    }

//    fun removeToolbar() {
//        removeToolbar(viewBinding.root as? ViewGroup)
//    }

    open fun toolbarFunc(curActivity: Activity?, toolbar: Toolbar?) {}
    open fun bindView() {}
    open fun bindViewModel() {}

//    override fun onShowPopup(popup: PopUp?) {
//        hideSoftKeyboard()
//        onClosePopup()
//        val popupDialogFragment =
//            if (popup?.isBottomSheet == true)
//                BottomPopupDialog.newInstance(popup)
//            else
//                PopupDialog.newInstance(popup ?: PopUp())
//
//        popupDialogFragment.show(supportFragmentManager, PopupDialog().tag)
//        listOfPopupDialogFragment = listOfPopupDialogFragment.plus(popupDialogFragment)
//    }
//
//    override fun onClosePopup() {
//        for (item in listOfPopupDialogFragment) {
//            item.dismissAllowingStateLoss()
//            listOfPopupDialogFragment = listOfPopupDialogFragment.minus(item)
//        }
//    }

//    override fun onDestroy() {
//        AppEvent.unRegisterPopupEventListener(this)
//        super.onDestroy()
//    }
}