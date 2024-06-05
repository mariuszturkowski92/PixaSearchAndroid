package com.andmar.common.domain.results


sealed interface Result<out T, out E : com.andmar.common.utils.error.Error> {
    data class Success<out T, out E : Error>(val data: T) : Result<T, E>
    data class Failure<out T, out E : Error>(val error: E) : Result<T, E>
}