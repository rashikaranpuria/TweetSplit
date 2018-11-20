package com.rashikaranpuria.tweetsplit.di.component

import com.rashikaranpuria.tweetsplit.di.module.AppModule
import com.rashikaranpuria.tweetsplit.di.module.TweetsActivityModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun tweetsActivityComponent(tweetsActivityModule: TweetsActivityModule): TweetsActivityComponent
}