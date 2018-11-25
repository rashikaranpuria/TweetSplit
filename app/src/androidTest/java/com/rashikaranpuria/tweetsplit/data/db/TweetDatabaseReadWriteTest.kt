package com.rashikaranpuria.tweetsplit.data.db

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.rashikaranpuria.tweetsplit.data.db.entity.Tweet
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class TweetDatabaseReadWriteTest {
    private lateinit var database: TweetDatabase
    private lateinit var dao: TweetDao

    @Before
    fun setup() {
        //create an in-memory version of database as it is hermetic
        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), TweetDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = database.tweetDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeTweetAndRead() {
        val tweet = Tweet(text = dummyString1)
        val id = dao.insertTweet(tweet)
        assertThat(dao.getTweetById(id).blockingFirst().text, equalTo(tweet.text))
    }

    @Test
    @Throws(Exception::class)
    fun writeAndReadTweets() {
        val tweets = listOf(Tweet(text = dummyString2), Tweet(text = dummyString3))
        dao.insertAllTweets(tweets)
        val allTweets = dao.getAllTweets().blockingFirst()
        assert(allTweets.count { it.text == dummyString2 } == 1)
        assert(allTweets.count { it.text == dummyString3 } == 1)
    }

    companion object {
        const val dummyString1 = "some dummy tweets"
        const val dummyString2 = "some dummy tweet 2"
        const val dummyString3 = "some dummy tweet 3"
    }

}