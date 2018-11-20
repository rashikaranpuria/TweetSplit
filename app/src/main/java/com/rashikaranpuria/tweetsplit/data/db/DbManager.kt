package com.rashikaranpuria.tweetsplit.data.db

import com.rashikaranpuria.tweetsplit.data.db.entity.Tweet
import io.reactivex.Flowable
import javax.inject.Inject

class DbManager @Inject constructor(val mTweetDao: TweetDao): IDbManager {
    override fun getAllTweets(): Flowable<List<Tweet>> {
        return mTweetDao.getAllTweets()
    }

    override fun addTweets(tweets: List<Tweet>) {
        mTweetDao.insertAllTweets(tweets)
    }

    override fun addTweet(tweet: Tweet) {
        mTweetDao.insertTweet(tweet)
    }

}
