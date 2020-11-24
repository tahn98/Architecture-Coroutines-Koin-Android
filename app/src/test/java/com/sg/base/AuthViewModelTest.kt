package com.sg.base

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.*
import androidx.test.espresso.matcher.ViewMatchers
import com.sg.base.viewmodel.AuthViewModel
import com.sg.core.model.Result
import com.sg.core.model.User
import com.sg.core.param.LoginParam
import com.sg.core.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AuthViewModelTest {

    @get:Rule
    val instantTestExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repo: AuthRepository
    @Mock
    lateinit var observer: Observer<User>

    private lateinit var authViewModel: AuthViewModel
    private val user = User(email = "jason@vinova.sg")
    private val loginParam: LoginParam = LoginParam(
        "jason@vinova.sg",
        "123123",
        "dGKlJJU1lCc:APA91bGZTz25rKtcb5WobysyPQSUp0Bfp4w1hblFjgWQeGdCEZwgFmRTCTQX9vhDk2WazWcvwpOHn8MV4NyTjrgE5vFEraxP5GbAMOnqYmo6FyVGy924yS98pEYSJXBJZ_5g_56nIFuC"
    )
    private var repoData: LiveData<Result<User>>? = null

    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        authViewModel = AuthViewModel(repo)
        runBlockingTest {
            val liveData: LiveData<Result<User>> = MutableLiveData(Result.Success(user))
            `when`(
                repo.login(
                    loginParam
                )
            ).thenReturn(liveData)
            repoData = repo.login(
                loginParam
            )
        }
        authViewModel.loginLiveData.observeForever(observer)

    }

    @Test
    fun login() {
        ViewMatchers.assertThat(
            "Text Failure",
            repoData?.value,
            Matchers.`is`(Result.Success(user))
        )
        authViewModel.loginLiveData.value = (repoData?.value as Result.Success).data

        verify(observer).onChanged(user)
    }

}