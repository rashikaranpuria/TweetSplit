package com.rashikaranpuria.tweetsplit.data.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.rashikaranpuria.tweetsplit.data.db.entity.Tweet
import io.reactivex.Flowable

@Dao
interface TweetDao {
    @Query("SELECT * FROM tweet")
    fun getAllTweets(): Flowable<List<Tweet>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllTweets(tweets: List<Tweet>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTweet(tweet: Tweet): Long

    @Query("SELECT * FROM tweet WHERE id = :id")
    fun getTweetById(id: Long): Flowable<Tweet>
}