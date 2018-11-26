package com.rashikaranpuria.tweetsplit

import android.app.Application
import com.facebook.stetho.Stetho
import com.rashikaranpuria.tweetsplit.di.component.AppComponent
import com.rashikaranpuria.tweetsplit.di.component.DaggerAppComponent
import com.rashikaranpuria.tweetsplit.di.module.AppModule

open class TweetApplication : Application() {
    lateinit var appComponent: AppComponent

    open var isInTest = false

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG && isInTest) {
            Stetho.initializeWithDefaults(this)
        }
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}
