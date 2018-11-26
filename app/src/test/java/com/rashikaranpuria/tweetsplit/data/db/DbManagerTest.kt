package com.rashikaranpuria.tweetsplit.data.db

import com.rashikaranpuria.tweetsplit.FakeApplication
import com.rashikaranpuria.tweetsplit.data.db.entity.Tweet
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.robolectric.annotation.Config

@Config(application = FakeApplication::class)
class DbManagerTest {

    @Mock
    lateinit var mTweetDao: TweetDao

    @Mock
    lateinit var tweets: List<Tweet>

    @InjectMocks
    lateinit var mDbManager: DbManager

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun getAllTweets() {
        // when db manager get all tweets called
        mDbManager.getAllTweets()
        // then tweet dao get all tweets called
        verify(mTweetDao).getAllTweets()
    }

    @Test
    fun addTweets() {
        // when db manager all tweets called
        mDbManager.addTweets(tweets)
        // then it calls tweet dao insert all tweets
        verify(mTweetDao).insertAllTweets(tweets)
    }

    @Test
    fun addTweet() {
        val tweet = Tweet(text = "some dummy string")
        // when db manager add tweet called
        mDbManager.addTweet(tweet)
        // then db manager insert tweet called
        verify(mTweetDao).insertTweet(tweet)
    }
}