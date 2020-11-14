package com.sg.base.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.bluelinelabs.conductor.archlifecycle.LifecycleController

abstract class BaseController<V : ViewDataBinding> : LifecycleController(), ViewModelStoreOwner {

    @get:LayoutRes
    abstract val layoutId: Int

    protected lateinit var viewBinding: V
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedViewState: Bundle?
    ): View {
        viewBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        bindingView()
        return viewBinding.root
    }

    override fun getViewModelStore(): ViewModelStore {
        return (activity as BaseActivity<*>).viewModelStore
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        setUpViewModel()
    }

    open fun bindingView() {}

    open fun setUpViewModel() {}

    open fun setUpViewModelOnce() {}
}