package com.namnguyen.cryptocurrency.di

import com.namnguyen.cryptocurrency.main.MainViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val AppModule = module {

    viewModel { MainViewModel(get()) }

    single { createGetPostsUseCase(get()) }

    single { createPostRepository(get()) }
}