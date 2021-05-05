package com.sg.core.repository.impl

import BasePositionPagingSource
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import com.sg.core.api.ApiService
import com.sg.core.data.local.LocalBoundResource
import com.sg.core.data.local.MessageDao
import com.sg.core.data.local.RemoteKeysDao
import com.sg.core.data.local.UserDao
import com.sg.core.data.remote.BasePageKeyPagingSource
import com.sg.core.data.remote.NetworkBoundResource
import com.sg.core.model.*
import com.sg.core.repository.AuthRepository
import com.sg.core.param.LoginParam
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import retrofit2.Response

class AuthRepositoryImpl(
    val api: ApiService,
    val userDao: UserDao,
    val messageDao: MessageDao,
    val remoteKeysDao: RemoteKeysDao
) :
    AuthRepository {

    override suspend fun login(param: LoginParam): Flow<Result<User>> {
        return object : NetworkBoundResource<ObjectResponse<User>, User>() {

            override suspend fun createCall(): Response<ObjectResponse<User>> = api.login(param)

            override fun processResponse(response: ObjectResponse<User>): User? = response.data

//            override suspend fun saveCallResult(item: User?) {
//                userDao.insert(item)
//            }

//            override suspend fun loadFromDb(): Flow<User>? {
//                return  userDao.getUserFlow()
////                    .map { it }
//            }

        }.buildAsFlow()

    }

//    override suspend fun loginDB(param: LoginParam): LiveData<Result<User>> {
//        return object : LocalBoundResource<User, User>() {
//
//            override suspend fun loadFromDb(): User? {
//                return userDao.findUser(param.username)
//            }
//
//            override suspend fun processResponse(response: User?): User? {
//                val user = User(email = param.username)
//                userDao.insert(User(email = param.username))
//                return user
//            }
//
//        }.build().asLiveData()
//    }
//

    override suspend fun message(keyword: String, page: Int) = Pager(PagingConfig(25)) {
        object : BasePageKeyPagingSource<Message, Message>() {
            override suspend fun createCall(page: Int): Response<ListResponse<Message>> =
                api.getMessage(keyword = keyword, page = page)

            override suspend fun handleResponse(items: ListResponse<Message>): List<Message> {
                messageDao.insertAll(items.data)
                return items.data
            }

        }
    }.flow

    override suspend fun movies(page: Int): Flow<PagingData<Movie>> = Pager(PagingConfig(25)) {
        object : BasePageKeyPagingSource<Movie, Movie>() {
            override suspend fun createCall(page: Int): Response<ListResponse<Movie>> =
                api.getMovies(page)

            override suspend fun handleResponse(items: ListResponse<Movie>): List<Movie> =
                items.data

        }
    }.flow

//    @ExperimentalPagingApi
//    override suspend fun messageDB(): Flow<PagingData<Message>> {

//        val pagingSourceFactory = {
//            messageDao.getMessagesPaging()
//        }
//        return Pager(
//            config = PagingConfig(pageSize = 25),
//            pagingSourceFactory = pagingSourceFactory,
//            remoteMediator = object : BasePositionPagingSource<Message>() {
//                override suspend fun createCall(page: Int): Response<ListResponse<Message>> {
//                    return api.getMessage(keyword = "", page = page)
//                }
//
//                override suspend fun getLastRemoteKey(state: PagingState<Int, Message>): RemoteKeys? {
//                    return state.pages
//                        .lastOrNull { it.data.isNotEmpty() }
//                        ?.data?.lastOrNull()
//                        ?.let {
//                            remoteKeysDao.remoteKeysId(it.id)
//                        }
//                }
//
//                override suspend fun getFirstRemoteKey(state: PagingState<Int, Message>): RemoteKeys? {
//                    return state.pages
//                        .firstOrNull() {
//                            it.data.isNotEmpty() }
//                        ?.data?.firstOrNull()
//                        ?.let { it -> remoteKeysDao.remoteKeysId(it.id) }
//                }
//
//                override suspend fun getClosestRemoteKey(state: PagingState<Int, Message>): RemoteKeys? {
//                    return state.anchorPosition?.let { position ->
//                        state.closestItemToPosition(position)?.id?.let { repoId ->
//                            remoteKeysDao.remoteKeysId(repoId)
//                        }
//                    }
//                }
//
//                override suspend fun mapRemoteKeys(
//                    data: List<Message>?,
//                    nextKey: Int?,
//                    preKey: Int?
//                ): List<RemoteKeys>? {
//                    messageDao.insertAll(data ?: arrayListOf())
//                    return data?.map {
//                        RemoteKeys(repoId = it.id, prevKey = preKey, nextKey = nextKey)
//                    }
//                }
//
//                override suspend fun clearRemoteKeys() {
//                    remoteKeysDao.clearRemoteKeys()
//                }
//
//                override suspend fun insertRemoteKeys(keys: List<RemoteKeys>?) {
//                    remoteKeysDao.insertAll(keys ?: arrayListOf())
//                }
//            }
//        ).flow
//        BasePositionPagingSource
//    }

    override suspend fun messageDB(): Flow<PagingData<Message>> = Pager(PagingConfig(25)) {
        object : BasePositionPagingSource<Message>() {
            override suspend fun createCall(loadSize: Int): List<Message> {
                return messageDao.getMessages()
            }
        }
    }.flow

}