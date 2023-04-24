package com.duoduo.mcqlogin.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LoginService {
    @GET("/cgi-bin/rad_user_info")
    suspend fun userInfo(
        @Query("callback")
        callback: String = "f"
    ): Response<UserInfo>

    @GET("/cgi-bin/get_challenge")
    suspend fun getChallenge(
        @Query("username") username: String,
        @Query("ip") ip: String,
        @Query("callback") callback: String = "f"
    ): Response<Challenge>

    @GET("/cgi-bin/srun_portal")
    suspend fun srunPortal(
        @Query("callback") callback: String,
        @Query("action") action: String,
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("os") os: String,
        @Query("name") name: String,
        @Query("double_stack") doubleStack: String,
        @Query("chksum") chksum: String,
        @Query("info") info: String,
        @Query("ac_id") acid: String,
        @Query("ip") ip: String,
        @Query("n") n: String,
        @Query("type") type: String
    ): Response<Login>
}