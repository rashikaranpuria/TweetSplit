package com.rashikaranpuria.tweetsplit.ui.tweets

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.rashikaranpuria.tweetsplit.BuildConfig
import com.rashikaranpuria.tweetsplit.FakeApplication
import com.rashikaranpuria.tweetsplit.data.db.entity.Tweet
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, application = FakeApplication::class, sdk = [21])
class TweetsAdapterTest {

    lateinit var context: Context
    lateinit var adapter: TweetsAdapter

    @Before
    fun setUp() {
        context = RuntimeEnvironment.application
        adapter = TweetsAdapter()
    }

    @Test
    fun itemCount() {
        val tweets = listOf(Tweet(id = 0, text = "abc"), Tweet(id = 1, text = "bcd"), Tweet(id = 2, text = "cde"))
        adapter.tweetList = tweets
        assertEquals(adapter.itemCount, tweets.size)
    }

    @Test
    fun onCreateViewHolder_returnsCorrectViewHolder() {
        val tweets = listOf(Tweet(id = 0, text = "abc"), Tweet(id = 1, text = "bcd"), Tweet(id = 2, text = "cde"))
        adapter.tweetList = tweets
        val rv = RecyclerView(context)
        rv.layoutManager = LinearLayoutManager(context)
        val viewHolder = adapter.onCreateViewHolder(rv, 0)
        assertTrue(viewHolder is TweetsAdapter.TweetViewHolder)
    }
}