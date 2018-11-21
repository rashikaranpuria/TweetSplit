package com.rashikaranpuria.tweetsplit.ui.tweets

import android.app.ProgressDialog
import android.os.Bundle
import android.support.design.R.attr.layoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.EditText
import com.rashikaranpuria.tweetsplit.R
import com.rashikaranpuria.tweetsplit.TweetApplication
import com.rashikaranpuria.tweetsplit.data.db.IDbManager
import com.rashikaranpuria.tweetsplit.data.db.entity.Tweet
import com.rashikaranpuria.tweetsplit.di.module.TweetsActivityModule
import com.rashikaranpuria.tweetsplit.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_tweets.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import javax.inject.Inject

class TweetsActivity: BaseActivity(), ITweetsView {

    @Inject
    lateinit var mTweetsActivityPresenter: ITweetsActivityPresenter<ITweetsView>

    @Inject
    override
    lateinit var mProgressDialog: ProgressDialog

    @Inject
    lateinit var mTweetsAdapter: TweetsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tweets)

        (application as TweetApplication).appComponent.tweetsActivityComponent(TweetsActivityModule(this)).inject(this)
        initOnCLickListeners()
        initTweetsAdapter()

        mTweetsActivityPresenter.onAttach(this)
    }

    private fun initOnCLickListeners() {
        new_tweet.onClick {
            alert {
                titleResource = R.string.new_tweet_title
                customView {
                    verticalLayout {
                        gravity = Gravity.CENTER
                        padding = dip(16)

                        val tweetEditText = editText {
                            hint = "Type your tweet..."
                            lines = 3
                        }

                        linearLayout {
                            textAlignment = right
                            negativeButton(R.string.cancel) {}
                            positiveButton(R.string.tweet) {
                                if (tweetEditText.text.isBlank()) {
                                    toast(R.string.empty_input_error)
                                }
                                else {
                                    mTweetsActivityPresenter.newTweetPostButtonClicked(tweetEditText.text.toString())
                                }
                            }
                        }
                    }
                }
            }.show()
        }
    }

    private fun initTweetsAdapter() {
        tweets_list.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@TweetsActivity)
            adapter = mTweetsAdapter
        }
    }

    override fun showTweets(tweets: List<Tweet>) {
        empty_view.visibility = View.GONE
        mTweetsAdapter.tweetList = tweets
    }

    override fun showEmptyView() {
        empty_view.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        mTweetsActivityPresenter.onDetach()
        mProgressDialog.dismiss()
    }
}