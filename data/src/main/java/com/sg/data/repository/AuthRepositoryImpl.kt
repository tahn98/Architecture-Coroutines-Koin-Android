package com.sg.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.toLiveData
import com.sg.data.api.ApiService
import com.sg.data.datasource.local.LocalBoundResource
import com.sg.data.datasource.remote.BaseDataSourceFactory
import com.sg.data.datasource.remote.BaseSource
import com.sg.data.datasource.remote.NetworkBoundResource
import com.sg.domain.util.PrefUtil
import com.sg.domain.entity.Message
import com.sg.domain.param.LoginParam
import com.sg.domain.common.ListResponse
import com.sg.domain.common.ObjectResponse
import com.sg.domain.common.Result
import com.sg.domain.common.Listing
import com.sg.domain.dao.MessageDao
import com.sg.domain.dao.UserDao
import com.sg.domain.entity.User
import com.sg.domain.repository.AuthRepository
import retrofit2.Response

class AuthRepositoryImpl(
    val api: ApiService,
    val userDao: UserDao,
    val messageDao: MessageDao,
    val prefUtil: PrefUtil
) :
    AuthRepository {

    override suspend fun login(param: LoginParam): LiveData<Result<User>> {
        return object : NetworkBoundResource<ObjectResponse<User>, User>() {

            override suspend fun createCall(): Response<ObjectResponse<User>> = api.login(param)

            override fun processResponse(response: ObjectResponse<User>): User? = response.data

            override suspend fun saveCallResult(item: User?) {
                userDao.insert(item)
            }

        }.build().asLiveData()
    }

    override suspend fun loginDB(param: LoginParam): LiveData<Result<User>> {
        return object : LocalBoundResource<User, User>() {

            override suspend fun loadFromDb(): User? {
                return userDao.findUser(param.email)
            }

            override suspend fun processResponse(response: User?): User? {
                val user = User(email = param.email)
                userDao.insert(User(email = param.email))
                return user
            }

        }.build().asLiveData()
    }

    override suspend fun message(page: Int): Listing<Message> {
        val status = MutableLiveData<Result<Message>>()

        val sourceFactory = object : BaseDataSourceFactory<Message, Message>(status) {
            override suspend fun createXCall(page: Int): Response<ListResponse<Message>> {
                return api.getMessage(page = page)
            }

            override suspend fun handleXResponse(
                items: ListResponse<Message>,
                firstLoad: Boolean
            ): List<Message> {
                items.data?.forEach {
                    messageDao.insert(it)
                }
                return super.handleXResponse(items, firstLoad)
            }

            override fun isNetworkConnected(): Boolean = prefUtil.isNetworkConnected()
        }

        val pagedList = sourceFactory.toLiveData(pageSize = 11)

        return Listing(result = pagedList, status = status, refresh = {
            sourceFactory.sourceLiveData.value?.invalidate()
        })
    }

    override suspend fun messageDB(): Listing<Message> {
        val status = MutableLiveData<Result<Message>>()

        val messages = messageDao.getMessages()
        val sourceFactory = object : BaseSource<Message>(messages) {}
        val pagedList = sourceFactory.toLiveData(pageSize = 11)
        return Listing(result = pagedList, status = status, refresh = {
            sourceFactory.sourceLiveData.value?.invalidate()
        })
    }

}