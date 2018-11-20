package com.rashikaranpuria.tweetsplit.ui.tweets

import com.rashikaranpuria.tweetsplit.data.db.entity.Tweet
import com.rashikaranpuria.tweetsplit.ui.base.IBaseView

interface ITweetsView: IBaseView {
    fun showTweets(tweets: List<Tweet>)
    fun showEmptyView()
}