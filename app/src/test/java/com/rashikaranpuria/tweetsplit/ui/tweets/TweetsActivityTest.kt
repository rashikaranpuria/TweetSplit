package com.rashikaranpuria.tweetsplit.ui.tweets

import android.app.Dialog
import android.app.ProgressDialog
import android.view.View
import com.rashikaranpuria.tweetsplit.BuildConfig
import com.rashikaranpuria.tweetsplit.FakeApplication
import com.rashikaranpuria.tweetsplit.R
import com.rashikaranpuria.tweetsplit.data.db.entity.Tweet
import junit.framework.Assert.assertEquals
import kotlinx.android.synthetic.main.activity_tweets.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowAlertDialog
import org.robolectric.shadows.ShadowToast

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, application = FakeApplication::class, sdk = [21])
class TweetsActivityTest {

    @Mock
    lateinit var tweets: List<Tweet>

    @Mock
    lateinit var mProgressDialog: ProgressDialog

    @Mock
    lateinit var mTweetsActivityPresenter: TweetsActivityPresenter<ITweetsView>

    @InjectMocks
    lateinit var mTweetActivity: TweetsActivity

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        mTweetActivity = Robolectric.buildActivity(TweetsActivity::class.java).create().get()
        mTweetActivity.mTweetsActivityPresenter = mTweetsActivityPresenter
    }

    @Test
    fun testShowTweets_RemovesEmptyView() {
        // when m tweet activity show tweets called
        mTweetActivity.showTweets(tweets)
        assertEquals(mTweetActivity.empty_view.visibility, View.GONE)
    }

    @Test
    fun showEmptyVuew_ChangesVisibilityOfEmptyView() {
        // when m tweet activity show tweets called
        mTweetActivity.showEmptyView()
        assertEquals(mTweetActivity.empty_view.visibility, View.VISIBLE)
    }

    @Test
    fun testDialogShownWithCorrectTitle_WhenNewTweetButtonClicked() {
        // when new tweet button clicked
        mTweetActivity.new_tweet.performClick()
        // then alert dialog opens
        val alertDialog = ShadowAlertDialog.getLatestAlertDialog()
        val sAlert = shadowOf(alertDialog)
        // alert dialog has correct title
        assertEquals(sAlert.title, mTweetActivity.resources.getString(R.string.new_tweet_title))
    }

    @Test
    fun testMPresenterMethodNewTweetPostButtonCalled_WhenNewTweetButtonClicked() {
        // get dialog
        mTweetActivity.new_tweet.performClick()
        val alertDialog = ShadowAlertDialog.getLatestAlertDialog()
        // when positive button of dialog clicked
        alertDialog.getButton(Dialog.BUTTON_POSITIVE).performClick()
        // then newTweetPostButton of tweet presenter called
        verify(mTweetsActivityPresenter).newTweetPostButtonClicked("")
    }

    @Test
    fun progressDialogShown_WhenShowProgressDialogCalled() {
        // given progress dialog
        mTweetActivity.mProgressDialog = mProgressDialog
        // when show progress bar called
        mTweetActivity.showProgressBar()
        // verify progress dialog becomes visible
        Mockito.verify(mProgressDialog).show()
    }

    @Test
    fun progressDialogHidden_WhenHideProgressDialogCalled() {
        // given
        mTweetActivity.mProgressDialog = mProgressDialog
        // when
        mTweetActivity.hideProgressDialog()
        // verify
        Mockito.verify(mProgressDialog).hide()
    }

    @Test
    fun toastShown_WhenShowErrorWithTextCalled() {
        // given error text
        val errorStr = "error"
        // when show error with text
        mTweetActivity.showError(errorStr)
        // verify toast has same text
        Assert.assertEquals(ShadowToast.getTextOfLatestToast(), errorStr)
    }

    @Test
    fun toastShown_WhenShowErrorWithIdCalled() {
        // given error id string res
        val errId = R.string.dummy_string
        // when show error with id called
        mTweetActivity.showError(errId)
        // verify toast has same text as id res
        Assert.assertEquals(ShadowToast.getTextOfLatestToast(), mTweetActivity.resources.getText(errId))
    }

    @Test
    fun toastShown_WhenShowMessageWithIdCalled() {
        // given msg string
        val msgId = R.string.dummy_string
        // when show message with message id called
        mTweetActivity.showMessage(msgId)
        // verify toast has same text as id res
        Assert.assertEquals(ShadowToast.getTextOfLatestToast(), mTweetActivity.resources.getText(msgId))
    }

}