package com.sg.base.view

import android.content.Context
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.sg.base.R
import com.sg.core.CoreApplication
import junit.framework.TestCase
import org.hamcrest.Matchers.instanceOf
import org.hamcrest.Matchers.notNullValue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Func InstrumentationRegistry.getInstrumentation()
 * @get [context] or [targetContext]
 * Need to use [targetContext]
 *
 * [ensureActivityIsPresent] is check [mActivity] is [LoginActivity]
 * [ensureButtonIsPresent] is check Button is AppcompatButton, check [isDisplayed], check text == "LOGIN" and click
 * [flowLogin] is automation UI test
 *
 * */

@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
class LoginActivityTest {

    @get:Rule
    var mActivityRule = ActivityTestRule(LoginActivity::class.java)

    private lateinit var context: Context
    private lateinit var mApplication: CoreApplication
    private val latch = CountDownLatch(1)

    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        mApplication = CoreApplication.instance
    }

    private fun ensureActivityIsPresent() {
        val mActivity: LoginActivity = mActivityRule.activity
        assertThat(mActivity, notNullValue())
        assertThat("Activity Failure", mActivity, instanceOf(LoginActivity::class.java))

    }

    private fun ensureEditTextIsPresent() {
        val mActivity: LoginActivity = mActivityRule.activity
        val edtEmail: View = mActivity.findViewById(R.id.edtEmail)
        val edtPassword: View = mActivity.findViewById(R.id.edtPassword)
        assertThat("Edit Text Email Failure", edtEmail, notNullValue())
        assertThat("Edit Text Password Failure", edtPassword, notNullValue())
        assertThat("Edit Text Email Failure", edtEmail, instanceOf(AppCompatEditText::class.java))
        assertThat(
            "Edit Text Email Failure",
            edtPassword,
            instanceOf(AppCompatEditText::class.java)
        )

    }

    private fun ensureButtonIsPresent() {
        val mActivity: LoginActivity = mActivityRule.activity
        val btnLogin: View = mActivity.findViewById(R.id.btnLogin)
        assertThat("Button Failure", btnLogin, notNullValue())
        assertThat("Button Failure", btnLogin, instanceOf(AppCompatButton::class.java))
    }

    @Test
    fun flowLogin() {
        ensureActivityIsPresent()
        ensureEditTextIsPresent()
        ensureButtonIsPresent()
        Espresso.onView(withId(R.id.edtEmail))
            .check(ViewAssertions.matches(isDisplayed()))
            .perform(
                typeText("jason@vinova.sg"), closeSoftKeyboard()
            )
        Espresso.onView(withId(R.id.edtPassword))
            .check(ViewAssertions.matches(isDisplayed()))
            .perform(
                typeText("123123"), closeSoftKeyboard()
            )
        Espresso.onView(withId(R.id.btnLogin))
            .check(ViewAssertions.matches(isDisplayed()))
            .check(ViewAssertions.matches(isClickable())).perform(click())
        latch.await(1, TimeUnit.SECONDS)
        val user = mApplication.getUser()
        TestCase.assertEquals(
            "Login Status - ", user?.email,
            "jason@vinova.sg"
        )
    }

}