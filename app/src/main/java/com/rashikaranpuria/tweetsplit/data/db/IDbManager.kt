package com.rashikaranpuria.tweetsplit.data.db

import com.rashikaranpuria.tweetsplit.data.db.entity.Tweet
import io.reactivex.Flowable

interface IDbManager {
    fun getAllTweets(): Flowable<List<Tweet>>
    fun addTweets(tweets: List<Tweet>)
    fun addTweet(tweet: Tweet)
}