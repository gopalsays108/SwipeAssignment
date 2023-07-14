package com.gopal.servicelayer.common

sealed class BaseApiResponse<out T> {

    data class Success<out R>(val data:R?): BaseApiResponse<R>()
    data class Failure(val msg: String): BaseApiResponse<Nothing>()
    object Loading: BaseApiResponse<Nothing>()
}