package com.sg.base.view

import android.content.Context
import android.os.Message
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.MergeAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.sg.base.R
import com.sg.base.adapter.MessagePagedAdapter
import com.sg.base.util.RecyclerViewMatcher
import com.sg.core.CoreApplication
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
class MainActivityTest {

    @get:Rule
    var mActivityRule = ActivityTestRule(MainActivity::class.java)

    private lateinit var context: Context
    private lateinit var mApplication: CoreApplication
    private val latch = CountDownLatch(1)

    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        mApplication = CoreApplication.instance
    }

    private fun ensureTextIsPresent() {
        val mActivity: MainActivity = mActivityRule.activity
        val tvTitle: View = mActivity.findViewById(R.id.tvTitle)
        ViewMatchers.assertThat("Text Failure", tvTitle, Matchers.notNullValue())
        ViewMatchers.assertThat(
            "Text Failure",
            tvTitle,
            Matchers.instanceOf(AppCompatTextView::class.java)
        )
        ViewMatchers.assertThat(
            "Text User Name Failure",
            (tvTitle as AppCompatTextView).text,
            Matchers.equalTo(tvTitle.text)
        )
    }

    private fun ensureRecyclerViewIsPresent() {
        val mActivity: MainActivity = mActivityRule.activity
        val rvMessages: View = mActivity.findViewById(R.id.rvMessage)
        ViewMatchers.assertThat("List Failure", rvMessages, Matchers.notNullValue())
        ViewMatchers.assertThat(
            "List Failure",
            rvMessages,
            Matchers.instanceOf(RecyclerView::class.java)
        )
        ViewMatchers.assertThat(
            "Adapter Failure",
            (rvMessages as RecyclerView).adapter,
            Matchers.instanceOf(MergeAdapter::class.java)
        )
        Espresso.onView(ViewMatchers.withId(R.id.rvMessage))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        latch.await(1, TimeUnit.SECONDS)
        ViewMatchers.assertThat(
            "Count List Failure",
            rvMessages.adapter?.itemCount,
            Matchers.greaterThanOrEqualTo(10)
        )
    }

    private fun ensureItemRecyclerViewIsPresent() {
        val mActivity: MainActivity = mActivityRule.activity
        val rvMessages: RecyclerView = mActivity.findViewById(R.id.rvMessage)
        val mergeAdapter: MergeAdapter = rvMessages.adapter as MergeAdapter
        ViewMatchers.assertThat(
            "Adapter Paging Failure",
            mergeAdapter.adapters[0],
            Matchers.instanceOf(MessagePagedAdapter::class.java)
        )
        val messageAdapter: MessagePagedAdapter = mergeAdapter.adapters[0] as MessagePagedAdapter
        Espresso.onView(RecyclerViewMatcher(R.id.rvMessage).atPosition(0, R.id.tvId)).check(
            ViewAssertions.matches(
                ViewMatchers.withText(messageAdapter.currentList?.get(0)?.id)
            )
        )
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun flowMain() {
        ensureTextIsPresent()
        ensureRecyclerViewIsPresent()
        ensureItemRecyclerViewIsPresent()
    }

}