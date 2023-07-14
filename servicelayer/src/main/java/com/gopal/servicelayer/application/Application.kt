package com.gopal.servicelayer.application

import android.app.Application
import com.gopal.servicelayer.product.module.ProductModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
//
//open class Application : Application() {
//
//    override fun onCreate() {
//        super.onCreate()
//        startKoin{
//            androidContext(this@Application)
//            modules(ProductModule)
//        }
//    }
//}