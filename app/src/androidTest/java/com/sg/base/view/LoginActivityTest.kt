package com.sg.base.view

import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.sg.base.R
import org.hamcrest.Matchers.instanceOf
import org.hamcrest.Matchers.notNullValue
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
class LoginActivityTest {

    @get:Rule
    var mActivityRule = ActivityTestRule(LoginActivity::class.java)

//    companion object{
//        @BeforeClass
//        fun setup(){
//
//        }
//    }

    @Test
    fun ensureButtonIsPresent() {
        val mActivity : LoginActivity = mActivityRule.activity
        assertThat(mActivity, notNullValue())
        val btnLogin: View = mActivity.findViewById<View>(R.id.btnLogin)
        assertThat(btnLogin, notNullValue())
        assertThat(btnLogin, instanceOf(AppCompatButton::class.java))
    }
}