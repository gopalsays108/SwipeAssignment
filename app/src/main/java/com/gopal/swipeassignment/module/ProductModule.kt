package com.gopal.swipeassignment.module

import com.gopal.servicelayer.product.repositories.ProductRepository
import com.gopal.servicelayer.product.repositories.ProductRepositoryImpl
import com.gopal.swipeassignment.product.viewmodel.ProductViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

var ProductModuleMain = module {

    single {
        ProductViewModel(get(),androidContext())
    }
}