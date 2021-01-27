package com.sg.core.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.toLiveData
import com.sg.core.api.ApiService
import com.sg.core.data.local.LocalBoundResource
import com.sg.core.data.local.MessageDao
import com.sg.core.data.local.UserDao
import com.sg.core.data.remote.BaseDataSourceFactory
import com.sg.core.data.remote.BaseSource
import com.sg.core.data.remote.NetworkBoundResource
import com.sg.core.domain.*
import com.sg.core.domain.param.LoginParam
import com.sg.core.domain.response.ListResponse
import com.sg.core.domain.response.ObjectResponse
import com.sg.core.domain.response.Result
import com.sg.core.domain.vo.Listing
import retrofit2.Response

class AuthRepositoryImpl(val api: ApiService, val userDao: UserDao, val messageDao: MessageDao) :
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
                return userDao.findUser(param.username)
            }

            override suspend fun processResponse(response: User?): User? {
                val user = User(email = param.username)
                userDao.insert(User(email = param.username))
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