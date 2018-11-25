package com.rashikaranpuria.tweetsplit.ui.tweets

import com.rashikaranpuria.tweetsplit.ImmediateSchedulerRule
import com.rashikaranpuria.tweetsplit.R
import com.rashikaranpuria.tweetsplit.data.DataManager
import com.rashikaranpuria.tweetsplit.data.db.entity.Tweet
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.*
import org.mockito.Mockito.*
import org.robolectric.shadows.ShadowToast
import org.mockito.Mockito.`when` as _when

@RunWith(Enclosed::class)
class TweetsActivityPresenterTest {

    @RunWith(Parameterized::class)
    class ParameterizedTweetsActivityPresenterUnitTests(private val tweetText: String, private val splitTweets: Array<String>) {
        companion object {
            @JvmStatic
            @Parameterized.Parameters
            fun tweetTexts() = listOf(
                arrayOf("A simple tweet that does need splitting in two parts, as soon as possible."
                    , arrayOf("1/2 A simple tweet that does need splitting in two", "2/2 parts, as soon as possible.")),
                arrayOf("A simple tweet that does need splitting in two parts, as soon as possible and still more."
                    , arrayOf("1/2 A simple tweet that does need splitting in two", "2/2 parts, as soon as possible and still more."))
            )
        }

        @Mock
        lateinit var mDataManager: DataManager

        @Mock
        lateinit var mCompositeDisposable: CompositeDisposable

        @Mock
        lateinit var mTweetsActivity: ITweetsView

        @Spy
        @InjectMocks
        lateinit var mTweetsActivityPresenter: TweetsActivityPresenter<ITweetsView>

        @Before
        fun setup() {
            MockitoAnnotations.initMocks(this)
        }

        @Test
        fun whenSplitMessageCalled_thenVerifyMessageSplitCorrectly() {
            assertEquals(mTweetsActivityPresenter.splitMessage(tweetText), splitTweets)
        }
    }

    class NonParameterizedTweetsActivityPresenterUnitTests {
        @get:Rule
        var immediateSchedulerRule = ImmediateSchedulerRule()

        @Mock
        lateinit var mDataManager: DataManager

        @Mock
        lateinit var mCompositeDisposable: CompositeDisposable

        @Mock
        lateinit var mTweetsActivity: ITweetsView

        @Mock
        lateinit var tweets: List<Tweet>

        @Spy
        @InjectMocks
        lateinit var mTweetsActivityPresenter: TweetsActivityPresenter<ITweetsView>

        @Before
        fun setup() {
            MockitoAnnotations.initMocks(this)
            _when(mDataManager.getAllTweets()).thenReturn(Flowable.just(tweets))
        }

        @Test
        fun whenSplitMessageCalled_thenVerifyMessageSplitCorrectly() {
            val tweet = "A simple tweet that does need splitting in two parts, as soon as possible."
            // when splitMessage called with tweet of length below threshold
            val splitTweets = mTweetsActivityPresenter.splitMessage(tweet)
            // then verify tweet returned as it is in an array
            assertEquals(splitTweets.size, 2)
            assertEquals(splitTweets, arrayOf("1/2 A simple tweet that does need splitting in two", "2/2 parts, as soon as possible."))
        }

        @Test
        fun whenGetTweetsCalled_VerifyBehaviourForDifferentTweetLength() {
            // case tweet length less than equal to 50
            var tweet = "abc abc abc abc abc"
            // when get tweets called
            var tweetList = mTweetsActivityPresenter.getTweets(tweet)
            // then returned tweets is same as passed but in a list
            assertEquals(tweetList.size, 1)
            assertEquals(tweetList[0].text, tweet)

            // case tweet length greater than 50
            tweet = "abc abc abc abc abc abc abc abc abc abc abc abc abc abc abc"
            tweetList = mTweetsActivityPresenter.getTweets(tweet)
            assertTrue(tweetList.size > 1)
            verify(mTweetsActivityPresenter).splitMessage(tweet)
        }

        @Test
        fun whenPresenterAttached_thenGetAllTweetsFromDataManagerCalled() {
            // when presenter attached to tweet activity
            mTweetsActivityPresenter.onAttach(mTweetsActivity)
            // then fetch all tweets method called
            verify(mDataManager).getAllTweets()
        }

        @Test
        fun whenPresenterDetached_thenCompositeDisposableIsDisposed() {
            // when presenter is detached
            mTweetsActivityPresenter.onDetach()
            // then composite disposable is disposed
            verify(mCompositeDisposable).dispose()
        }

        @Test
        fun newPostButtonClickedCalled_thenBehaviourForEmptyTweetText() {
            // setup presenter on attach
            mTweetsActivityPresenter.onAttach(mTweetsActivity)

            // when presenter newPostButtonClickedCalled with blank tweet text
            mTweetsActivityPresenter.newTweetPostButtonClicked("")
            // then verify error message and data manager add tweets is not called
            verify(mTweetsActivity).showError(R.string.empty_input_error)
            verify(mDataManager, never()).addTweets(ArgumentMatchers.anyList())
        }

        @Test
        fun newPostButtonClickedCalled_thenBehaviourForNonEmptyTweetText() {
            // setup presenter on attach
            mTweetsActivityPresenter.onAttach(mTweetsActivity)

            // when presenter newPostButtonClickedCalled with non blank tweet text
            mTweetsActivityPresenter.newTweetPostButtonClicked(dummyTweetText)
            // then verify error message and data manager add tweets is not called
            verify(mTweetsActivity, never()).showError(R.string.empty_input_error)
            verify(mDataManager).addTweets(ArgumentMatchers.anyList())
        }

        companion object {
            const val dummyTweetText = "abc abc abc abc abc abc abc abc abc abc abc abc "
        }
    }


}