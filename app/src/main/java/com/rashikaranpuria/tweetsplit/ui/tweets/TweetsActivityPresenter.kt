package com.rashikaranpuria.tweetsplit.ui.tweets

import android.util.Log
import com.rashikaranpuria.tweetsplit.R
import com.rashikaranpuria.tweetsplit.data.IDataManager
import com.rashikaranpuria.tweetsplit.data.db.entity.Tweet
import com.rashikaranpuria.tweetsplit.ui.base.BasePresenter
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

const val TWEET_LENGTH = 50
class TweetsActivityPresenter<V: ITweetsView> @Inject constructor(val mCompositeDisposable: CompositeDisposable, val mDataManager: IDataManager): BasePresenter<V> (), ITweetsActivityPresenter<V> {

    override fun newTweetPostButtonClicked(tweetText: String) {
        view?.showProgressBar()
        val tweets = getTweets(tweetText)
        if (tweets.isNotEmpty()) {
            val s = Completable.fromAction {
                mDataManager.addTweets(tweets)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy (
                onComplete = {
                    view?.showMessage(R.string.tweet_success_msg)
                    view?.hideProgressDialog()
                },
                onError = {
                    view?.showError(it.localizedMessage)
                    view?.hideProgressDialog()
                }
            )
        }
        else {
            view?.hideProgressDialog()
        }
    }

    private fun getTweets(tweetText: String): List<Tweet> {
        val n = tweetText.length
        return if (n <= TWEET_LENGTH) {
            listOf(Tweet(text = tweetText))
        } else {
            splitMessage(tweetText).mapIndexed { idx, str ->  Tweet(text = str) }
        }
    }

    private fun splitMessage(s: String): List<String> {
        val n = s.length
        var numParts = (n/TWEET_LENGTH) + 1
        var numPartLen = numParts.toString().length
        val extra = (numParts*(3+numPartLen))/50 + 2
        numParts += extra
        numPartLen = numParts.toString().length
        val splitMsgList = mutableListOf<String>()
        val cur = StringBuilder("")
        var num = 1
        var i = 0
        while (i < n) {
            val rem = TWEET_LENGTH - 2 - num.toString().length - numPartLen
            if (i + rem >= n) {
                cur.append(s.substring(i..(s.lastIndex)))
                i += rem
            }
            else if (s[i+rem-1] == ' ') {
                cur.append(s.substring(i..(i+rem-2)))
                i += rem
            }
            else if (s[i+rem] == ' ') {
                cur.append(s.substring(i..(i+rem-1)))
                i += rem + 1
            }
            else {
                //go backtrack
                var j = i + rem - 1
                while (j > i && s[j] != ' ') j--
                if (j == i) {
                    view?.showError(R.string.unable_to_parse_error)
                    break
                }
                cur.append(s.substring(i..(j-1)))
                i = j + 1

            }
            splitMsgList.add(cur.toString())
            cur.setLength(0)
            num++
        }
        if (i > n) num--
        var l = 1
        for (k in 0..splitMsgList.lastIndex)
        {
            cur.setLength(0)
            cur.append("$l/$num ${splitMsgList[k]}")
            splitMsgList[k] = cur.toString()
            if (splitMsgList[k].length > 50) {
                Log.d("SPLIT", "LENGTH ERROR in >${splitMsgList[k]}")
            }
            l++
        }
        return splitMsgList
    }

    override fun onAttach(v: V) {
        super.onAttach(v)
        fetchAllTweets()
    }

    private fun fetchAllTweets() {
        view?.showProgressBar()
        mCompositeDisposable.add(
            mDataManager
                .getAllTweets()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy (
                    onNext = {
                        if (it.isEmpty()) {
                            view?.showEmptyView()
                        }
                        else {
                            view?.showTweets(it)
                        }
                        view?.hideProgressDialog()
                    },
                    onError = {
                        view?.showError(it.localizedMessage)
                        view?.hideProgressDialog()
                    }
                ))
    }

    override fun onDetach() {
        super.onDetach()
        mCompositeDisposable.dispose()
    }
}