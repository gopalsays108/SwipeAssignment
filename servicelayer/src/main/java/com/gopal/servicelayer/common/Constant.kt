package com.gopal.servicelayer.common

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

fun <T> result(call: suspend () -> Response<T>): Flow<BaseApiResponse<T?>> = flow {

    emit(BaseApiResponse.Loading)
    try {
        val c = call()
        c.let { response ->
            if (c.isSuccessful) {
                emit(BaseApiResponse.Success(response.body()))
            } else {
                c.errorBody()?.let { responseBody ->
                    responseBody.close()
                    emit(BaseApiResponse.Failure(responseBody.toString()))
                }
            }
        }
    } catch (e: Exception) {
        Log.e("CONSTANT", "result: Error is: ${e.message}")
        emit(BaseApiResponse.Failure(e.message.toString()))
    }
}