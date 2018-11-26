package com.rashikaranpuria.tweetsplit.ui.tweets

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

const val TWEET_LENGTH_THRESHOLD = 50
class TweetsActivityPresenter<V : ITweetsView> @Inject constructor(val mCompositeDisposable: CompositeDisposable, val mDataManager: IDataManager) : BasePresenter<V> (), ITweetsActivityPresenter<V> {

    override fun newTweetPostButtonClicked(tweetText: String) {
        if (tweetText.isEmpty() || tweetText.isBlank()) {
            view?.showError(R.string.empty_input_error)
            return
        } else {
            view?.showProgressBar()
            val tweets = getTweets(tweetText)
            if (tweets.isNotEmpty()) {
                val s = Completable.fromAction {
                    mDataManager.addTweets(tweets)
                }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(
                        onComplete = {
                            view?.showMessage(R.string.tweet_success_msg)
                            view?.hideProgressDialog()
                        },
                        onError = {
                            view?.showError(it.localizedMessage)
                            view?.hideProgressDialog()
                        }
                    )
            } else {
                view?.hideProgressDialog()
            }
        }
    }

    fun getTweets(tweetText: String): List<Tweet> {
        val n = tweetText.length
        return if (n <= TWEET_LENGTH_THRESHOLD) {
            listOf(Tweet(text = tweetText))
        } else {
            splitMessage(tweetText).map { str -> Tweet(text = str) }
        }
    }

    fun splitMessage(msg: String): Array<String> {
        val wordsInMsg = msg.split(" ")
        val parts = calculatePartsInTweets(wordsInMsg, wordsInMsg.size)
        if (parts == -1) {
            view?.showError(R.string.unable_to_parse_error)
            return arrayOf()
        }
        return getShortTweetListsInParts(wordsInMsg, parts)
    }

    private fun getShortTweetListsInParts(wordsInMsg: List<String>, parts: Int): Array<String> {
        val listOfShortTweets = mutableListOf<String>()
        var currentPart = 1
        var i = 0
        var lengthOfThisPart = ""
        while (i < wordsInMsg.size) {
            val partIndicator = if (lengthOfThisPart == "") {
                (currentPart.toString() + "/" + parts)
            } else {
                ""
            }
            lengthOfThisPart += partIndicator
            while (i < wordsInMsg.size && lengthOfThisPart.length <= TWEET_LENGTH_THRESHOLD) {
                val nextWordAttached = lengthOfThisPart + " " + wordsInMsg[i]
                if (nextWordAttached.length > TWEET_LENGTH_THRESHOLD) {
                    break
                } else {
                    lengthOfThisPart = nextWordAttached
                    i++
                }
            }
            listOfShortTweets.add(lengthOfThisPart)
            currentPart++
            lengthOfThisPart = ""
        }
        return listOfShortTweets.toTypedArray()
    }

    private fun calculatePartsInTweets(wordsInMsg: List<String>, partsUpperBound: Int): Int {
        var parts = 2
        while (!isValidPart(wordsInMsg, parts) && parts < partsUpperBound) {
            parts++
        }
        return if (parts >= partsUpperBound) {
            -1
        } else {
            parts
        }
    }

    private fun isValidPart(wordsInMsg: List<String>, parts: Int): Boolean {
        var currentPart = 1
        var i = 0
        var lengthOfThisPart = ""
        while (i<wordsInMsg.size) {
            val partIndicator = if (lengthOfThisPart == "") {
                (currentPart.toString() + "/" + parts)
            } else {
                ""
            }
            lengthOfThisPart += partIndicator
            while (i < wordsInMsg.size && lengthOfThisPart.length <= TWEET_LENGTH_THRESHOLD) {
                lengthOfThisPart += " " + wordsInMsg[i]
                if (lengthOfThisPart.length > TWEET_LENGTH_THRESHOLD) {
                    break
                } else {
                    i++
                }
            }
            if (lengthOfThisPart == partIndicator) {
                return false
            } else {
                currentPart++
                lengthOfThisPart = ""
            }
        }
        return currentPart - 1 == parts
    }

// a less accurate split message method
//    private fun splitMessage(s: String): List<String> {
//        val n = s.length
//        var numParts = (n/TWEET_LENGTH_THRESHOLD) + 1
//        var numPartLen = numParts.toString().length
//        val extra = (numParts*(3+numPartLen))/50 + 2
//        numParts += extra
//        numPartLen = numParts.toString().length
//        val splitMsgList = mutableListOf<String>()
//        val cur = StringBuilder("")
//        var num = 1
//        var i = 0
//        while (i < n) {
//            val rem = TWEET_LENGTH_THRESHOLD - 2 - num.toString().length - numPartLen
//            if (i + rem >= n) {
//                cur.append(s.substring(i..(s.lastIndex)))
//                i += rem
//            }
//            else if (s[i+rem-1] == ' ') {
//                cur.append(s.substring(i..(i+rem-2)))
//                i += rem
//            }
//            else if (s[i+rem] == ' ') {
//                cur.append(s.substring(i..(i+rem-1)))
//                i += rem + 1
//            }
//            else {
//                //go backtrack
//                var j = i + rem - 1
//                while (j > i && s[j] != ' ') j--
//                if (j == i) {
//                    view?.showError(R.string.unable_to_parse_error)
//                    break
//                }
//                cur.append(s.substring(i..(j-1)))
//                i = j + 1
//
//            }
//            splitMsgList.add(cur.toString())
//            cur.setLength(0)
//            num++
//        }
//        if (i > n) num--
//        var l = 1
//        for (k in 0..splitMsgList.lastIndex)
//        {
//            cur.setLength(0)
//            cur.append("$l/$num ${splitMsgList[k]}")
//            splitMsgList[k] = cur.toString()
//            if (splitMsgList[k].length > TWEET_LENGTH_THRESHOLD) {
//                Log.d("SPLIT", "LENGTH ERROR in >${splitMsgList[k]}")
//            }
//            l++
//        }
//        return splitMsgList
//    }

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
                .subscribeBy(
                    onNext = {
                        if (it.isEmpty()) {
                            view?.showEmptyView()
                        } else {
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