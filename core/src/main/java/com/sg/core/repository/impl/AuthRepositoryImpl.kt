package com.sg.core.repository.impl

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
import com.sg.core.model.*
import com.sg.core.repository.AuthRepository
import com.sg.core.param.LoginParam
import com.sg.core.vo.Listing
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.Response

class AuthRepositoryImpl(val api: ApiService, val userDao: UserDao, val messageDao: MessageDao) :
    AuthRepository {

    override suspend fun login(param: LoginParam): LiveData<Result<User>> {
        return object : NetworkBoundResource<ObjectResponse<User>, User>() {

            override suspend fun createCall(): Flow<Response<ObjectResponse<User>>> = flow {
                emit(api.login(param))
            }

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