package com.rashikaranpuria.tweetsplit.di.component

import com.rashikaranpuria.tweetsplit.di.module.TweetsActivityModule
import com.rashikaranpuria.tweetsplit.ui.tweets.TweetsActivity
import dagger.Subcomponent

@Subcomponent(modules = [TweetsActivityModule::class])
interface TweetsActivityComponent {
    fun inject(tweetsActivity: TweetsActivity)
}