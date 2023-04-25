package com.duoduo.mcqlogin.api

data class UserInfo(
    val user_name: String,
    val online_ip: String,
    val client_ip: String,
    val error: String,
    val sum_bytes: Long,
)

data class Challenge(
    val challenge: String
)

data class Login(
    val error: String,
    val error_msg: String,
    val srun_ver: String,
    val res: String,
)

data class LoginUserInfo(
    val acid: String,
    val enc_ver: String,
    val ip: String,
    val password: String,
    val username: String,
)