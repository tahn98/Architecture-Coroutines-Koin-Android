package com.sg.base.di

import com.sg.base.viewmodel.AuthViewModel
import com.sg.base.viewmodel.MovieViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModules = module {
    viewModel {
        MovieViewModel(get())
    }
}