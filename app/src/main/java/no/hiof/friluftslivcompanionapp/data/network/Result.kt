package no.hiof.friluftslivcompanionapp.data.network

sealed class Result<out T> {
    data class Success<out T>(val value: T): Result<T>()
    data class Failure(val message: String?): Result<Nothing>()
}
