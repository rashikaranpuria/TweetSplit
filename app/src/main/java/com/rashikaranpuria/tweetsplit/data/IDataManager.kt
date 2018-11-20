package com.rashikaranpuria.tweetsplit.data

import com.rashikaranpuria.tweetsplit.data.db.entity.Tweet
import io.reactivex.Flowable

interface IDataManager {

    fun getAllTweets(): Flowable<List<Tweet>>
    fun addTweets(tweets: List<Tweet>)
    fun addTweet(tweet: Tweet)
}