package com.rashikaranpuria.tweetsplit.ui.tweets

import com.rashikaranpuria.tweetsplit.ui.base.IBasePresenter

interface ITweetsActivityPresenter<V : ITweetsView> : IBasePresenter<V> {
    fun newTweetPostButtonClicked(tweetText: String)
}