package com.sg.presentation.base

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.sg.presentation.ext.setAutoHideKeyboard

abstract class BaseActivity<ViewBinding : ViewDataBinding> : AppCompatActivity(){
    lateinit var viewBinding: ViewBinding

    @get:LayoutRes
    abstract val layoutId: Int
    @get:LayoutRes
    open val toolbarLayoutId: Int = -1

//    private var listOfPopupDialogFragment: Set<DialogFragment> =
//        CopyOnWriteArraySet<DialogFragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        AppEvent.addPopupEventListener(this)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        viewBinding = DataBindingUtil.setContentView(this, layoutId)
        viewBinding.lifecycleOwner = this
        viewBinding.root.setAutoHideKeyboard(this)
        bindView()
        bindViewModel()
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