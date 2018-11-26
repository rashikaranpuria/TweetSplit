package com.rashikaranpuria.tweetsplit.ui.tweets

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.rashikaranpuria.tweetsplit.R
import com.rashikaranpuria.tweetsplit.data.db.entity.Tweet
import kotlin.properties.Delegates

class TweetsAdapter : RecyclerView.Adapter<TweetsAdapter.TweetViewHolder>(), AutoUpdatableAdapter {

    var tweetList: List<Tweet> by Delegates.observable(emptyList()) {
            _, oldList, newList ->
        autoNotify(oldList, newList) { o, n -> o.id == n.id }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweetViewHolder =
        TweetViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.tweet_item, parent, false))

    override fun getItemCount(): Int = tweetList.size

    override fun onBindViewHolder(holder: TweetViewHolder, p1: Int) {
        holder.bind(tweetList[holder.adapterPosition])
    }

    class TweetViewHolder(private val v: View) : RecyclerView.ViewHolder(v) {
        val tweetTextView = v.findViewById(R.id.text) as TextView

        fun bind(tweet: Tweet) {
            tweetTextView.text = tweet.text
        }
    }
}