package com.mbh.moviebrowser.repository

import android.util.Log
import com.google.common.truth.Truth
import com.mbh.moviebrowser.api.TMDBApi
import com.mbh.moviebrowser.data.response.Genre
import com.mbh.moviebrowser.data.response.GenresResponse
import com.mbh.moviebrowser.db.DataStoreManager
import com.mbh.moviebrowser.db.GenreDao
import com.mbh.moviebrowser.util.Resource
import io.mockk.every
import io.mockk.mockkStatic
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import retrofit2.Response
import java.net.UnknownHostException

class GenresRepositoryTest {

    private val error401 = Response.error<GenresResponse>(
        401, "".toResponseBody(null)
    )

    private lateinit var repository: GenresRepository
    private val api: TMDBApi = mock()
    private val dao: GenreDao = mock()
    private val dataStoreManager: DataStoreManager = mock()

    private val mockGenreResponse = listOf(
        Genre(1, "Action"),
        Genre(2, "Sci-fi")
    )


    @Before
    fun setUp() {
        repository = GenresRepositoryImpl(api, dao, dataStoreManager)
        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 0
    }

    @Test
    fun `Get genres returns a list`() = runBlocking {
        whenever(api.getMovieGenreList()).thenReturn(
            Response.success(GenresResponse(mockGenreResponse))
        )

        whenever(dataStoreManager.getLastSyncTimestamp()).thenReturn(null)

        whenever(dao.getGenres()).thenReturn(mockGenreResponse)

        Truth.assertThat(repository.getGenreList().first()).isEqualTo(Resource.Loading)
        Truth.assertThat(
            repository.getGenreList().drop(1).first().data?.first()
        ).isEqualTo(
            Resource.Success(mockGenreResponse).data?.first()
        )
    }

    @Test
    fun `Test genre list returns from cache`() = runBlocking {
        whenever(dataStoreManager.getLastSyncTimestamp()).thenReturn(System.currentTimeMillis())

        whenever(dao.getGenres()).thenReturn(mockGenreResponse)

        Truth.assertThat(repository.getGenreList().first()).isEqualTo(Resource.Loading)
        Truth.assertThat(
            repository.getGenreList().drop(1).first().data?.first()
        ).isEqualTo(
            Resource.Success(mockGenreResponse).data?.first()
        )
    }

    @Test
    fun `Get genres returns a 401 error`() = runBlocking {
        whenever(api.getMovieGenreList()).thenReturn(
            error401
        )

        whenever(dataStoreManager.getLastSyncTimestamp()).thenReturn(null)

        Truth.assertThat(repository.getGenreList().first()).isEqualTo(Resource.Loading)
        Truth.assertThat(
            repository.getGenreList().drop(1).first()
        ).isEqualTo(
            Resource.Error.NetworkError(401)
        )
    }

    @Test
    fun `Api unknown host exception`() = runBlocking {
        whenever(api.getMovieGenreList()).doAnswer { throw UnknownHostException() }

        whenever(dataStoreManager.getLastSyncTimestamp()).thenReturn(null)

        Truth.assertThat(repository.getGenreList().first()).isEqualTo(Resource.Loading)
        Truth.assertThat(
            repository.getGenreList().drop(1).first()
        ).isEqualTo(
            Resource.Error.UnknownHostError
        )
    }

    @Test
    fun `Api unknown exception`() = runBlocking {
        whenever(api.getMovieGenreList()).doAnswer { throw Exception() }

        whenever(dataStoreManager.getLastSyncTimestamp()).thenReturn(null)

        Truth.assertThat(repository.getGenreList().first()).isEqualTo(Resource.Loading)
        Truth.assertThat(
            repository.getGenreList().drop(1).first()
        ).isEqualTo(
            Resource.Error.UnknownError
        )
    }
}