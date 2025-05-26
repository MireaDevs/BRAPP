package ru.pro.beatrate.domain.beatrate_backend

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.pro.beatrate.data.models.Session
import ru.pro.beatrate.domain.beatrate_backend.models.AuthResponse
import ru.pro.beatrate.domain.beatrate_backend.models.LoginRequest
import ru.pro.beatrate.domain.beatrate_backend.models.News
import ru.pro.beatrate.domain.beatrate_backend.models.RegisterRequest

interface ApiService {

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<Unit>

    @GET("api/news")
    suspend fun getAllNews(): List<News>

    @POST("api/news")
    suspend fun createNews(@Body news: News): News

    @DELETE("api/news/{id}")
    suspend fun deleteNews(@Path("id") id: Long): Response<Unit>

    @POST("api/booking")
    suspend fun createBooking(
        @Header("Authorization") token: String,
        @Body session: Session
    ): Response<Unit>

    @GET("api/booking/user")
    suspend fun getBookingsByUser(
        @Header("Authorization") token: String,
        @Query("name") username: String
    ): List<Session>

}
