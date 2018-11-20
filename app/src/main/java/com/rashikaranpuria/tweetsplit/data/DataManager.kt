package com.rashikaranpuria.tweetsplit.data

import com.rashikaranpuria.tweetsplit.data.db.IDbManager
import com.rashikaranpuria.tweetsplit.data.db.entity.Tweet
import io.reactivex.Flowable
import javax.inject.Inject

class DataManager @Inject constructor (val mDbManager: IDbManager): IDataManager {
    override fun getAllTweets(): Flowable<List<Tweet>> {
        return mDbManager.getAllTweets()
    }

    override fun addTweets(tweets: List<Tweet>) {
        mDbManager.addTweets(tweets)
    }

    override fun addTweet(tweet: Tweet) {
        mDbManager.addTweet(tweet)
    }

}
