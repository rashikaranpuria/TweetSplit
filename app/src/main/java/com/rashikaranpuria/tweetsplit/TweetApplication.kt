package com.rashikaranpuria.tweetsplit

import android.app.Application
import com.rashikaranpuria.tweetsplit.di.component.AppComponent
import com.rashikaranpuria.tweetsplit.di.component.DaggerAppComponent
import com.rashikaranpuria.tweetsplit.di.module.AppModule
import dagger.Component

class TweetApplication: Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}