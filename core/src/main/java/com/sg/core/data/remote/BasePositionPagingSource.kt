package com.sg.core.data.remote

import android.util.Log
import androidx.paging.*
import androidx.room.Entity
import androidx.room.withTransaction
import com.google.gson.Gson
import com.sg.core.CoreApplication
import com.sg.core.data.local.AppDatabase
import com.sg.core.data.local.BaseDao
import com.sg.core.data.local.RemoteKeysDao
import com.sg.core.data.remote.BasePageKeyPagingSource.Companion.DEFAULT_PAGE_INDEX
import com.sg.core.model.ListResponse
import com.sg.core.model.RemoteKeys
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.io.InvalidObjectException

@ExperimentalPagingApi
abstract class BasePositionPagingSource<Dao :BaseDao<Any>, I : Any>(private val dao : Dao) :
    RemoteMediator<Int, I>() {

    open fun appDatabase(): AppDatabase {
        return AppDatabase.getInstance(CoreApplication.instance)
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, I>): MediatorResult {
        val page = when (val pageKeyData = getKeyPageData(loadType, state)) {
            is MediatorResult.Success -> {
                return pageKeyData
            }
            else -> {
                pageKeyData as Int
            }
        }

        try {
            val apiResponse = createCall((page))
            val isEndOfList = apiResponse.body()?.data?.isEmpty() ?: true
            appDatabase().withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    appDatabase().remoteKeysDao().clearRemoteKeys()
//                    dao.clear(I)
                }
                val prevKey = if (page == DEFAULT_PAGE_INDEX) null else page - 1
                val nextKey = if (isEndOfList) null else page + 1
                val keys = mapRemoteKeys(apiResponse.body()?.data, nextKey, prevKey)
//                database.getDoggoImageModelDao().insertAll(response)
//                database.getRepoDao().insertAll(keys)
            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    protected abstract suspend fun createCall(page: Int): Response<ListResponse<I>>

    /**
     * this returns the page key or the final end of list success result
     */
    private suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, I>): Any? {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getClosestRemoteKey(state)
                remoteKeys?.nextKey?.minus(1) ?: DEFAULT_PAGE_INDEX
            }
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                    ?: throw InvalidObjectException("Remote key should not be null for $loadType")
                remoteKeys.nextKey
            }
            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                    ?: throw InvalidObjectException("Invalid state, key should not be null")
                //end of list condition reached
                remoteKeys.prevKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                remoteKeys.prevKey
            }
        }
    }

    /**
     * get the last remote key inserted which had the data
     */
    protected abstract suspend fun getLastRemoteKey(state: PagingState<Int, I>): RemoteKeys?

    /**
     * get the first remote key inserted which had the data
     */
    protected abstract suspend fun getFirstRemoteKey(state: PagingState<Int, I>): RemoteKeys?

    /**
     * get the closest remote key inserted which had the data
     */
    protected abstract suspend fun getClosestRemoteKey(state: PagingState<Int, I>): RemoteKeys?

    /**
     * map response to remoteKeys
     */
    protected abstract fun mapRemoteKeys(data : List<I>?, nextKey : Int?, preKey : Int?): List<RemoteKeys>?

    /**
     * get the last remote key inserted which had the data
     */
//    private suspend fun getLastRemoteKey(state: PagingState<Int, I>): RemoteKeys? {
//        return state.pages
//            .lastOrNull { it.data.isNotEmpty() }
//            ?.data?.lastOrNull()
//            ?.let {
//                remoteKeysDao.remoteKeysId(it.id)
//            }
//    }

    /**
     * get the first remote key inserted which had the data
     */
//    private suspend fun getFirstRemoteKey(state: PagingState<Int, DoggoImageModel>): RemoteKeys? {
//        return state.pages
//            .firstOrNull() { it.data.isNotEmpty() }
//            ?.data?.firstOrNull()
//            ?.let { doggo -> remoteKeysDao.remoteKeysDoggoId(doggo.id) }
//    }

    /**
     * get the closest remote key inserted which had the data
     */
//    private suspend fun getClosestRemoteKey(state: PagingState<Int, DoggoImageModel>): RemoteKeys? {
//        return state.anchorPosition?.let { position ->
//            state.closestItemToPosition(position)?.id?.let { repoId ->
//                remoteKeysDao.remoteKeysDoggoId(repoId)
//            }
//        }
//    }

}