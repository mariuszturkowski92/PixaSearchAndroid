package com.andmar.common.utils.result

import com.andmar.common.utils.error.Error


sealed interface Result<out T, out E : Error> {

    data class Success<out T, out E : Error>(val data: T) : Result<T, E>
    data class Failure<out T, out E : Error>(val error: E, val data: T? = null) : Result<T, E>
}

inline fun <T, E : Error, F : Error> Result<T, E>.mapError(block: (E) -> F): Result<T, F> {
    return when (this) {
        is Result.Success -> Result.Success(data)
        is Result.Failure -> Result.Failure(block(error), data)
    }
}