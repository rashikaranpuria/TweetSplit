package com.rashikaranpuria.tweetsplit.data

import com.rashikaranpuria.tweetsplit.data.db.IDbManager
import com.rashikaranpuria.tweetsplit.data.db.entity.Tweet
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class DataManagerTest {

    @Mock
    lateinit var mDbManager: IDbManager

    @Mock
    lateinit var tweets: List<Tweet>

    @InjectMocks
    lateinit var mDataManager: DataManager

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun shouldCallDbManagerGetAppTweets_when_getAllTweets_called() {
        // when mDataManager.getAllTweetsCalled
        mDataManager.getAllTweets()
        // then verify mDbManager.getAllTweets called
        verify(mDbManager).getAllTweets()
    }

    @Test
    fun shouldCallMDbManagerAddTweets_when_addTweets_called() {
        // when mDataManager addTweets called
        mDataManager.addTweets(tweets)
        // then verify mDbManager addTweets called
        verify(mDbManager).addTweets(tweets)
    }

    @Test
    fun shouldCallMDbManagerAddTweet_when_addTweet_called() {
        val tweet = Tweet(text = "dummy text")
        // when mDataManager addTweets called
        mDataManager.addTweet(tweet)
        // then verify mDbManager addTweets called
        verify(mDbManager).addTweet(tweet)
    }
}