package com.sg.base.di

import com.sg.presentation.viewmodel.AuthViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        AuthViewModel(get())
    }
}