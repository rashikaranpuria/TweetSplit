package com.rashikaranpuria.tweetsplit.ui.tweets

import com.rashikaranpuria.tweetsplit.data.IDataManager
import com.rashikaranpuria.tweetsplit.data.db.entity.Tweet
import com.rashikaranpuria.tweetsplit.ui.base.BasePresenter
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TweetsActivityPresenter<V: ITweetsView> @Inject constructor(val mCompositeDisposable: CompositeDisposable, val mDataManager: IDataManager): BasePresenter<V> (), ITweetsActivityPresenter<V> {

    override fun newTweetPostButtonClicked(tweetText: String) {
        val s = Completable.fromAction {
            mDataManager.addTweet(Tweet(0, text = tweetText))
        }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeBy (
            onComplete = {
                view?.showError("Yay DONE!")
            },
            onError = {
                view?.showError("NOT DONE!!!")
            }
        )
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