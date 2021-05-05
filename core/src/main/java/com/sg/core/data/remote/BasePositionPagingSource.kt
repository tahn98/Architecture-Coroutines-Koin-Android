import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import java.io.IOException

abstract class BasePositionPagingSource<I : Any> : PagingSource<Int, I>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, I> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val response = createCall(params.loadSize)
            val paging = when {
                params.loadSize * position + params.loadSize > response.size -> response.subList(
                    params.loadSize * position,
                    response.size
                )
                else -> response.subList(
                    params.loadSize * position,
                    params.loadSize * position + params.loadSize
                )
            }
            Log.e("PAGING", position.toString() + "----" + params.loadSize * position + "-----" + response.size)
            LoadResult.Page(
                data = paging,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (paging.isNullOrEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, I>): Int? = null

    abstract suspend fun createCall(loadSize: Int): List<I>
}