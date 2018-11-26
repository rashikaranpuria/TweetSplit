package com.rashikaranpuria.tweetsplit.di.module

import android.content.Context
import com.rashikaranpuria.tweetsplit.R
import com.rashikaranpuria.tweetsplit.ui.tweets.ITweetsActivityPresenter
import com.rashikaranpuria.tweetsplit.ui.tweets.ITweetsView
import com.rashikaranpuria.tweetsplit.ui.tweets.TweetsActivityPresenter
import com.rashikaranpuria.tweetsplit.ui.tweets.TweetsAdapter
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import org.jetbrains.anko.indeterminateProgressDialog

@Module
class TweetsActivityModule(val context: Context) {

    @Provides
    fun providePresenter(presenter: TweetsActivityPresenter<ITweetsView>) = presenter as ITweetsActivityPresenter<ITweetsView>

    @Provides
    fun provideCompositeDisposable() = CompositeDisposable()

    @Provides
    fun provideProgressDialog() = context.indeterminateProgressDialog(context.getString(R.string.please_wait)).apply {
        hide()
    }

    @Provides
    fun provideTweetAdapter() = TweetsAdapter()
}