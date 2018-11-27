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

    /**
     * @param tweetText -> string of tweet text
     * Invoked by view when new tweet post button clicked inside new tweet dialog
     * handles empty tweet error case
     *
    */
    override fun newTweetPostButtonClicked(tweetText: String) {
        if (tweetText.isEmpty() || tweetText.isBlank()) {
            view?.showError(R.string.empty_input_error)
            return
        } else {
            view?.showProgressBar()
            // send tweet text after pre processing to remove any extra whitespaces
            val tweets = getTweets(preProcessString(tweetText))
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

    /**
     * @param tweetText -> String tweet text
     * @return List of tweets split into appropriate tweets within tweet length threshold
     */
    fun getTweets(tweetText: String): List<Tweet> {
        val n = tweetText.length
        return if (n <= TWEET_LENGTH_THRESHOLD) {
            listOf(Tweet(text = tweetText))
        } else {
            splitMessage(tweetText).map { str -> Tweet(text = str) }
        }
    }

    /**
     * @param str -> any string
     * @return Original string with extra white spaces at the end and in between words removed
     */
    private fun preProcessString(str: String): String = str.trim().replace("\\s+".toRegex(), " ")

    /**
     * @param msg -> tweet text
     * @return array of tweets within tweet length threshold
     */
    fun splitMessage(msg: String): Array<String> {
        val wordsInMsg = msg.split(" ")
        val parts = calculatePartsInTweets(wordsInMsg, wordsInMsg.size)
        if (parts == -1) {
            view?.showError(R.string.unable_to_parse_error)
            return arrayOf()
        }
        return getShortTweetListsInParts(wordsInMsg, parts)
    }

    /**
     * @param wordsInMsg -> words split by spaces
     * @param parts -> Number of parts in which we have to break the tweet
     * @return List of short tweets
     */
    private fun getShortTweetListsInParts(wordsInMsg: List<String>, parts: Int): Array<String> {
        val listOfShortTweets = mutableListOf<String>()
        var currentPart = 1
        var i = 0
        var wordsInThisPart = ""
        while (i<wordsInMsg.size) {
            // append partIndicator at start of a new part of tweet
            val partIndicator = if (wordsInThisPart == "") {
                (currentPart.toString() + "/" + parts)
            } else {
                ""
            }
            wordsInThisPart += partIndicator
            // Keep adding words to this part until we hit the end of the list of word,
            // or hit the threshold.
            while (i<wordsInMsg.size && wordsInThisPart.length <= TWEET_LENGTH_THRESHOLD) {
                val nextWordAttached = wordsInThisPart + " " + wordsInMsg[i]
                if (nextWordAttached.length> TWEET_LENGTH_THRESHOLD) {
                    break
                } else {
                    wordsInThisPart = nextWordAttached
                    i++
                }
            }
            // add this part to list
            listOfShortTweets.add(wordsInThisPart)
            currentPart++
            wordsInThisPart = ""
        }
        return listOfShortTweets.toTypedArray()
    }

    /**
     * @param wordsInMsg -> words split by spaces
     * @param partsUpperBound -> Upper bound to how many parts at max we can have for this tweet.
     * @return Number of arts in which this tweet can be broken
     */
    private fun calculatePartsInTweets(wordsInMsg: List<String>, partsUpperBound: Int): Int {
        // For every part p >= 2 and p <= upperbound, check if tweet can be broken into p parts correctly
        var parts = 2
        // Early check for some error cases
        if (parts >= partsUpperBound) {
            return -1
        }
        while (!isValidPart(wordsInMsg, parts) && parts<partsUpperBound) {
            parts++
        }
        // If no parts found <= upperbound, return -1 to show that tweet can't be broken
        return if (parts >= partsUpperBound) {
            -1
        } else {
            parts
        }
    }

    /**
     *@param wordsInMsg -> words split by spaces
     * @param parts -> Parts for which we'll check if words can be slit into
     * @return Boolean which reflects if the words can be broken into @param{parts} parts.
     *
     */
    private fun isValidPart(wordsInMsg: List<String>, parts: Int): Boolean {
        var currentPart = 1
        var i = 0
        var wordsInThisPart = ""
        while (i<wordsInMsg.size) {
            // currentPart can't be greater than total number of parts
            if (currentPart > parts) {
                return false
            }
            // If it's s start of nw tweet, append part indicator to it.
            val partIndicator = if (wordsInThisPart == "") {
                (currentPart.toString() + "/" + parts)
            } else {
                ""
            }
            wordsInThisPart += partIndicator
            // Keep adding words to this part until we
            // hit the end of word-list, or the threshold
            while (i<wordsInMsg.size && wordsInThisPart.length <= TWEET_LENGTH_THRESHOLD) {
                wordsInThisPart += " " + wordsInMsg[i]
                if (wordsInThisPart.length > TWEET_LENGTH_THRESHOLD) {
                    break
                } else {
                    i++
                }
            }
            // If no word is added to this part, it means
            // the word can't be broken further
            if (wordsInThisPart == partIndicator) {
                return false
            } else {
                // Work on next part
                currentPart++
                wordsInThisPart = ""
            }
        }
        return currentPart - 1 == parts
    }

    override fun onAttach(v: V) {
        super.onAttach(v)
        fetchAllTweets()
    }

    /**
     * @return all stored tweets in database
     */
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